package com.unisguard.webapi.service.user.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.dept.DeptDO;
import com.unisguard.webapi.common.dataobject.group.GroupUserDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import com.unisguard.webapi.common.dataobject.sysconfig.SysConfigDO;
import com.unisguard.webapi.common.dataobject.user.*;
import com.unisguard.webapi.common.dataobject.weakpass.WeakPassDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.common.util.CurrentUserUtil;
import com.unisguard.webapi.common.util.DeptUtil;
import com.unisguard.webapi.common.util.MenuUtil;
import com.unisguard.webapi.common.util.SessionUtil;
import com.unisguard.webapi.manage.dept.DeptManage;
import com.unisguard.webapi.manage.group.GroupUserManage;
import com.unisguard.webapi.manage.menu.MenuManage;
import com.unisguard.webapi.manage.sysconfig.SysConfigManage;
import com.unisguard.webapi.manage.user.UserHistoryManage;
import com.unisguard.webapi.manage.user.UserManage;
import com.unisguard.webapi.manage.user.UserRoleManage;
import com.unisguard.webapi.manage.weakpass.WeakPassManage;
import com.unisguard.webapi.service.user.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserManage userManage;
    @Resource
    private UserRoleManage userRoleManage;
    @Resource
    private GroupUserManage groupUserManage;
    @Resource
    private MenuManage menuManage;
    @Resource
    private DeptManage deptManage;
    @Resource
    private SysConfigManage sysConfigManage;
    @Resource
    private WeakPassManage weakPassManage;
    @Resource
    private UserHistoryManage userHistoryManage;

    @Override
    public ResponseDO<List<UserDO>> list(UserDO param) {
        // ????????????????????????????????????id
        if (param.getDeptId() != null && param.getDeptId() != 0) {
            List<Long> deptIdList = DeptUtil.getDeptId(deptManage.deptTree(), param.getDeptId());
            deptIdList.add(param.getDeptId());
            param.setDeptIdList(deptIdList);
        }
        Page page = PageHelper.startPage(param.getPage(), param.getLimit());
        List<UserDO> userList = userManage.list(param);
        if (CollectionUtils.isEmpty(userList)) {
            return ResponseDO.success(userList);
        }
        // ????????????id??????????????????
        List<UserRoleDO> userRoleList = userRoleManage.queryListByUserIds(userList.stream().map(UserDO::getId).collect(Collectors.toList()));
        // ????????????id??????
        Map<Long, List<UserRoleDO>> userRoleMap = userRoleList.stream().collect(Collectors.groupingBy(UserRoleDO::getUserId));
        userList.forEach(userDO -> userDO.setUserRoleList(userRoleMap.get(userDO.getId())));
        return ResponseDO.success(userList, page.getTotal());
    }

    @Override
    public ResponseDO<Long> add(UserDO param) {
        param.setUpdateUserId(CurrentUserUtil.getId());
        // ??????
        validateUser(param);
        // ????????????
        checkPassword(param);
        // ??????????????????
        param.setFirstLogin(DictConstant.DEFAULT);
        userManage.add(param);
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<UserDO> detail(Long id) {
        UserDO userDO = userManage.detail(id);
        if (userDO == null) {
            throw new ApplicationException("??????????????????");
        }
        // ????????????id??????????????????
        List<UserRoleDO> userRoleDOList = userRoleManage.queryListByUserId(id);
        userDO.setUserRoleList(userRoleDOList);
        // ????????????id???????????????
        List<GroupUserDO> groupUserDOList = groupUserManage.queryListByUserId(id);
        userDO.setGroupUserList(groupUserDOList);
        // ????????????id??????????????????
        List<Long> roleIdList = userRoleDOList.stream().map(UserRoleDO::getRoleId).collect(Collectors.toList());
        List<MenuDO> menuDOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roleIdList)) {
            menuDOList = menuManage.queryListByRoleIds(roleIdList);
        }
        List<MenuDO> treeMenu = MenuUtil.getTree(menuDOList, -1L);
        userDO.setMenuList(treeMenu);
        return ResponseDO.success(userDO);
    }

    @Override
    public ResponseDO<Long> edit(UserDO param) {
        param.setUpdateUserId(CurrentUserUtil.getId());
        param.setUpdateTime(LocalDateTime.now());
        // ??????
        validateUser(param);
        userManage.edit(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> delete(Long id) {
        userManage.delete(id);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO download(HttpServletResponse response, UserDO param) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // ??????URLEncoder.encode????????????????????????
            String excelName = URLEncoder.encode("????????????", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            Set<String> includeColumns = new HashSet<>();
            EasyExcel.write(response.getOutputStream(), UserExcelDO.class).includeColumnFiledNames(includeColumns).sheet("????????????").doWrite(userManage.queryExcelList(param));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<List<UserDO>> queryListByParam(UserParamDO param) {
        //??????
        return getUserList(param, true);
    }

    @Override
    public ResponseDO<List<UserDO>> queryListByParamNoPage(UserParamDO param) {
        //?????????
        return getUserList(param, false);
    }

    /**
     * ??????
     *
     * @param param
     */
    private void validateUser(UserDO param) {
        //????????????????????????
        UserDO userDO = userManage.checkUserName(param.getName());
        if (userDO != null && (param.getId() == null || !userDO.getId().equals(param.getId()))) {
            throw new ApplicationException("??????????????????");
        }
        userDO = userManage.checkUserAccount(param.getAccount());
        if (userDO != null && (param.getId() == null || !userDO.getId().equals(param.getId()))) {
            throw new ApplicationException("?????????????????????");
        }
        //?????????????????????????????????????????????
        String nameRegex = "^[A-Za-z\u4e00-\u9fa5]+$";
        if (!param.getName().matches(nameRegex)) {
            throw new ApplicationException("??????????????????????????????????????????");
        }
        //???????????????????????????????????????????????????
        String acountRegex = "^[A-Za-z0-9\u4e00-\u9fa5]+$";
        if (!param.getAccount().matches(acountRegex)) {
            throw new ApplicationException("?????????????????????????????????????????????");
        }
        //???????????????????????????????????????
        String titleRegex = "^[A-Za-z\u4e00-\u9fa5]+$";
        if (StringUtils.isNotBlank(param.getTitle()) && !param.getTitle().matches(titleRegex)) {
            throw new ApplicationException("???????????????????????????????????????");
        }

        String phoneRegex = "^(1[3|4|5|6|7|8|9])[0-9]{9}";
        if (param.getPhone() != null && param.getPhone() != 0 && !param.getPhone().toString().matches(phoneRegex)) {
            throw new ApplicationException("????????????????????????");
        }

        String emailRegex = "^([a-z]|[0-9]|_%|-|\\.)*@[a-z0-9]*\\.([a-z]{2,4})$";
        if (StringUtils.isNotBlank(param.getEmail()) && !param.getEmail().matches(emailRegex)) {
            throw new ApplicationException("????????????????????????");
        }
    }

    private void checkPassword(UserDO param) {
        // ????????????
        if (!param.getPassword().equals(param.getConfirmPassword())) {
            throw new ApplicationException("??????????????????????????????");
        }
        //????????????????????????
        if (Pattern.compile("[\u4e00-\u9fa5]").matcher(param.getPassword()).find()) {
            throw new ApplicationException("??????????????????????????????");
        }
        // ?????????????????????????????????????????????
        Map<Integer, String> map = sysConfigManage.list(new SysConfigDO()).stream().collect(Collectors.toMap(SysConfigDO::getDataKey, SysConfigDO::getDataValue, (k1, k2) -> k1));
        StringBuilder tips = new StringBuilder("??????????????????");
        // ?????????????????????
        if (StringUtils.isNotBlank(map.get(DictConstant.PASSWORD_COMPOSITION))) {
            // ????????????
            String passwordComposition = map.get(DictConstant.PASSWORD_COMPOSITION);
            PasswordCompositoinDo pwdComp = JSON.parseObject(passwordComposition, PasswordCompositoinDo.class);
            if (StringUtils.isNotBlank(pwdComp.getNumber()) && DictConstant.TRUE.toString().equals(pwdComp.getNumber())) {
                // ????????????????????????
                if (tips.length() == 6) {
                    tips.append("??????");
                } else {
                    tips.append(",??????");
                }
            }
            if (StringUtils.isNotBlank(pwdComp.getLowercase()) && DictConstant.TRUE.toString().equals(pwdComp.getLowercase())) {
                // ??????????????????????????????
                if (tips.length() == 6) {
                    tips.append("????????????");
                } else {
                    tips.append(",????????????");
                }
            }
            if (StringUtils.isNotBlank(pwdComp.getUppercase()) && DictConstant.TRUE.toString().equals(pwdComp.getUppercase())) {
                // ??????????????????????????????
                if (tips.length() == 6) {
                    tips.append("????????????");
                } else {
                    tips.append(",????????????");
                }
            }

            if (StringUtils.isNotBlank(pwdComp.getSpecial()) && StringUtils.isNotBlank(pwdComp.getSpecial_example()) && DictConstant.TRUE.toString().equals(pwdComp.getSpecial())) {
                if (tips.length() == 6) {
                    tips.append("???????????????").append(pwdComp.getSpecial_example());
                } else {
                    tips.append(",???????????????").append(pwdComp.getSpecial_example());
                }
            }

            if (StringUtils.isNotBlank(pwdComp.getNumber()) && DictConstant.TRUE.toString().equals(pwdComp.getNumber()) && !Pattern.compile("[1-9]+").matcher(param.getPassword()).find()) {
                // ????????????????????????
                throw new ApplicationException(tips.toString());
            }

            if (StringUtils.isNotBlank(pwdComp.getLowercase()) && DictConstant.TRUE.toString().equals(pwdComp.getLowercase()) && !Pattern.compile("[a-z]+").matcher(param.getPassword()).find()) {
                // ??????????????????????????????
                throw new ApplicationException(tips.toString());
            }

            if (StringUtils.isNotBlank(pwdComp.getUppercase()) && DictConstant.TRUE.toString().equals(pwdComp.getUppercase()) && !Pattern.compile("[A-Z]+").matcher(param.getPassword()).find()) {
                // ??????????????????????????????
                throw new ApplicationException(tips.toString());
            }

            if (StringUtils.isNotBlank(pwdComp.getSpecial()) && StringUtils.isNotBlank(pwdComp.getSpecial_example()) && DictConstant.TRUE.toString().equals(pwdComp.getSpecial())) {
                // ??????????????????????????????
                String reg = "[" + pwdComp.getSpecial_example().replace(",", "") + "]";
                if (!Pattern.compile(reg).matcher(param.getPassword()).find()) {
                    throw new ApplicationException(tips.toString());
                }
            }
        }
        if (StringUtils.isNotBlank(map.get(DictConstant.PASSWORD_LENGTH))) {
            // ????????????
            String[] lengths = map.get(DictConstant.PASSWORD_LENGTH).split("-");
            if (param.getPassword().length() < Integer.parseInt(lengths[0])) {
                throw new ApplicationException("????????????????????????" + Integer.parseInt(lengths[0]) + "???");
            }
            if (param.getPassword().length() > Integer.parseInt(lengths[1])) {
                throw new ApplicationException("????????????????????????" + Integer.parseInt(lengths[1]) + "???");
            }
        }
        if (StringUtils.isNotBlank(map.get(DictConstant.WEAK_PASSWORD)) && DictConstant.TRUE.toString().equals(map.get(DictConstant.WEAK_PASSWORD))) {
            // ?????????
            List<WeakPassDO> weakPassDOList = weakPassManage.list(new WeakPassDO());
            weakPassDOList.forEach(
                    weakPassDO -> {
                        if (param.getPassword().equals(weakPassDO.getPass())) {
                            throw new ApplicationException("???????????????????????????????????????????????????");
                        }
                    }
            );
        }
        if (StringUtils.isNotBlank(map.get(DictConstant.DISABLE_PERIOD_PASSWORD))
                && map.get(DictConstant.DISABLE_PERIOD_PASSWORD).equals(String.valueOf(DictConstant.TRUE))
                && StringUtils.isNotBlank(map.get(DictConstant.PASSWORD_CYCLE_IN_DISABLE_CYCLE))
                && StringUtils.isNotBlank(map.get(DictConstant.PASSWORD_VALIDITY_PERIOD)) && StringUtils.isNotBlank(map.get(DictConstant.PASSWORD_VALIDITY_UNIT))
        ) {
            LocalDateTime localDateTime = LocalDateTime.now();
            // ?????????????????????
            Long cycle = Long.parseLong(map.get(DictConstant.PASSWORD_CYCLE_IN_DISABLE_CYCLE));
            // ????????????????????????
            Long period = Long.parseLong(map.get(DictConstant.PASSWORD_VALIDITY_PERIOD));
            // ??????????????????????????????
            String unit = map.get(DictConstant.PASSWORD_VALIDITY_UNIT);
            if (unit.equals(GlobalConstant.YEAR)) {
                localDateTime = localDateTime.minusYears(cycle * period);
            } else {
                localDateTime = localDateTime.minusMonths(cycle * period);
            }
            // ????????????????????????????????????
            UserHistoryDO userHistoryDO = new UserHistoryDO();
            userHistoryDO.setUpdateTime(localDateTime);
            List<UserHistoryDO> userHistoryList = userHistoryManage.list(userHistoryDO);
            String md5Password = DigestUtils.md5Hex(param.getPassword());
            List<String> passwordList = userHistoryList.stream().map(UserHistoryDO::getPasswrod).collect(Collectors.toList());
            if (passwordList.size() > 0 && passwordList.contains(md5Password)) {
                throw new ApplicationException("?????????????????????????????????");
            }
        }
        param.setPassword(DigestUtils.md5Hex(param.getPassword()));
    }

    @Override
    public ResponseDO deleteBatch(List<Long> param) {
        userManage.deleteBatch(param);
        return ResponseDO.success();
    }

    @Override
    public ResponseDO<Long> updateStatus(UserDO param) {
        param.setUpdateUserId(CurrentUserUtil.getId());
        param.setUpdateTime(LocalDateTime.now());
        userManage.updateStatus(param);
        param = userManage.detail(param.getId());
        if (param == null) {
            throw new ApplicationException("??????????????????");
        }
        if (DictConstant.DISABLE.equals(param.getStatus())) {
            SessionUtil.remove(param.getAccount());
        }
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<Long> resetPassword(UserDO param) {
        param.setUpdateUserId(CurrentUserUtil.getId());
        param.setUpdateTime(LocalDateTime.now());
        UserDO userDO = userManage.info(param.getId());
        if (userDO == null) {
            throw new ApplicationException("??????????????????");
        }
        if (userDO.getPassword().equals(DigestUtils.md5Hex(param.getPassword()))) {
            throw new ApplicationException("????????????????????????????????????");
        }
        checkPassword(param);
        param.setUpdateTime(LocalDateTime.now());
        UserHistoryDO userHistoryDO = new UserHistoryDO();
        userHistoryDO.setUserId(userDO.getId());
        userHistoryDO.setPasswrod(userDO.getPassword());
        userHistoryDO.setUpdateTime(LocalDateTime.now());
        userHistoryManage.add(userHistoryDO);
        userManage.resetPassword(param);
        SessionUtil.remove(userDO.getAccount());
        return ResponseDO.success(param.getId());
    }

    @Override
    public ResponseDO<String> generatePassword() {
        // ?????????????????????????????????????????????
        Map<Integer, String> map = sysConfigManage.list(new SysConfigDO()).stream().collect(Collectors.toMap(SysConfigDO::getDataKey, SysConfigDO::getDataValue, (k1, k2) -> k1));
        // ?????????????????????????????? ??????12
        int length = 12;
        // ??????????????????
        String lengthMax = map.get(DictConstant.PASSWORD_LENGTH).split("-")[1];
        if (StringUtils.isNotBlank(lengthMax)) {
            length = Integer.parseInt(lengthMax);
        }
        // ??????????????????
        String passwordComposition = map.get(DictConstant.PASSWORD_COMPOSITION);
        PasswordCompositoinDo pwdComp = JSON.parseObject(passwordComposition, PasswordCompositoinDo.class);
        String rex = "";
        if (StringUtils.isNotBlank(pwdComp.getNumber()) && DictConstant.TRUE.toString().equals(pwdComp.getNumber())) {
            rex = rex + "1234567890";
        }
        if (StringUtils.isNotBlank(pwdComp.getLowercase()) && DictConstant.TRUE.toString().equals(pwdComp.getLowercase())) {
            // ??????????????????????????????
            rex = rex + "abcdefghijklmnopqrstuvwxyz";
        }
        if (StringUtils.isNotBlank(pwdComp.getUppercase()) && DictConstant.TRUE.toString().equals(pwdComp.getUppercase())) {
            // ??????????????????????????????
            rex = rex + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        if (StringUtils.isNotBlank(pwdComp.getSpecial()) && StringUtils.isNotBlank(pwdComp.getSpecial_example()) && DictConstant.TRUE.toString().equals(pwdComp.getSpecial())) {
            // ??????????????????????????????
            rex = rex + pwdComp.getSpecial_example().replaceAll(",", "");
        }
        // ????????????
        String password = RandomStringUtils.random(length, rex);
        // ????????????????????????????????????????????????????????????
        if (StringUtils.isNotBlank(map.get(DictConstant.WEAK_PASSWORD)) && DictConstant.TRUE.toString().equals(map.get(DictConstant.WEAK_PASSWORD))) {
            // ?????????
            List<String> weakPassDOList = weakPassManage.list(new WeakPassDO()).stream().map(WeakPassDO::getPass).collect(Collectors.toList());
            while (weakPassDOList.contains(password)) {
                password = RandomStringUtils.random(length, rex);
            }
        }
        // ???????????????????????????????????????
        while (!checkPasswordRex(pwdComp, password)) {
            password = RandomStringUtils.random(length, rex);
        }
        return ResponseDO.success(password);
    }

    private boolean checkPasswordRex(PasswordCompositoinDo pwdComp, String password) {
        if (StringUtils.isNotBlank(pwdComp.getNumber()) && DictConstant.TRUE.toString().equals(pwdComp.getNumber()) && !Pattern.compile("[1-9]+").matcher(password).find()) {
            //????????????????????????
            return false;
        }
        if (StringUtils.isNotBlank(pwdComp.getLowercase()) && DictConstant.TRUE.toString().equals(pwdComp.getLowercase()) && !Pattern.compile("[a-z]+").matcher(password).find()) {
            //??????????????????????????????
            return false;
        }
        if (StringUtils.isNotBlank(pwdComp.getUppercase()) && DictConstant.TRUE.toString().equals(pwdComp.getUppercase()) && !Pattern.compile("[A-Z]+").matcher(password).find()) {
            //??????????????????????????????
            return false;
        }
        if (StringUtils.isNotBlank(pwdComp.getSpecial()) && StringUtils.isNotBlank(pwdComp.getSpecial_example()) && DictConstant.TRUE.toString().equals(pwdComp.getSpecial())) {
            //??????????????????????????????
            String reg = "[" + pwdComp.getSpecial_example().replace(",", "") + "]";
            return Pattern.compile(reg).matcher(password).find();
        }
        return true;
    }

    @Override
    public ResponseDO<List<DeptDO>> deptTree() {
        List<DeptDO> deptDOList = deptManage.deptUserTree();
        deptDOList = DeptUtil.getTree(deptDOList, -2L);
        return ResponseDO.success(deptDOList);
    }

    private ResponseDO<List<UserDO>> getUserList(UserParamDO param, Boolean flag) {
        // ????????????????????????????????????id
        if (param.getDeptId() != null && param.getDeptId() != 0) {
            List<Long> deptIdList = DeptUtil.getDeptId(deptManage.deptTree(), param.getDeptId());
            deptIdList.add(param.getDeptId());
            param.setDeptIdList(deptIdList);
        }
        if (StringUtils.isNotBlank(param.getUserIds())) {
            param.setUserIdList(Arrays.stream(param.getUserIds().split(",")).map(Long::parseLong).collect(Collectors.toList()));
        }
        Page page = new Page();
        if (flag) {
            page = PageHelper.startPage(param.getPage(), param.getLimit());
        }
        List<UserDO> userList = userManage.queryListByParam(param);
        if (CollectionUtils.isEmpty(userList)) {
            return ResponseDO.success();
        }
        // ????????????id??????????????????
        List<UserRoleDO> userRoleList = userRoleManage.queryListByUserIds(userList.stream().map(UserDO::getId).collect(Collectors.toList()));
        // ????????????id??????
        Map<Long, List<UserRoleDO>> userRoleMap = userRoleList.stream().collect(Collectors.groupingBy(UserRoleDO::getUserId));
        // ????????????id???????????????
        List<GroupUserDO> groupUserList = groupUserManage.queryListByUserIds(userList.stream().map(UserDO::getId).collect(Collectors.toList()));
        Map<Long, List<GroupUserDO>> groupUserMap = groupUserList.stream().collect(Collectors.groupingBy(GroupUserDO::getUserId));
        userList.forEach(userDO -> {
            userDO.setUserRoleList(userRoleMap.get(userDO.getId()));
            userDO.setGroupUserList(groupUserMap.get(userDO.getId()));
        });
        return flag ? ResponseDO.success(userList, page.getTotal()) : ResponseDO.success(userList);
    }
}
