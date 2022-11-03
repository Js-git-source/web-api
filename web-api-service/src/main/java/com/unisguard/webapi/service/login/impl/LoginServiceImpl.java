package com.unisguard.webapi.service.login.impl;

import com.unisguard.webapi.common.constant.DictConstant;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.login.CurrentUserDO;
import com.unisguard.webapi.common.dataobject.login.LoginDO;
import com.unisguard.webapi.common.dataobject.menu.MenuDO;
import com.unisguard.webapi.common.dataobject.user.UserDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.common.util.BeanCopierUtil;
import com.unisguard.webapi.common.util.MenuUtil;
import com.unisguard.webapi.manage.menu.MenuManage;
import com.unisguard.webapi.manage.menu.MenuUrlManage;
import com.unisguard.webapi.manage.user.UserManage;
import com.unisguard.webapi.service.login.LoginService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zemel
 * @date 2022/1/9 20:16
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private UserManage userManage;
    @Resource
    private MenuManage menuManage;
    @Resource
    private MenuUrlManage menuUrlManage;

    @Override
    public CurrentUserDO login(LoginDO param, String authCode) {
        if (StringUtils.isBlank(param.getAccount()) || StringUtils.isBlank(param.getPassword())) {
            throw new ApplicationException("用户名或者密码错误");
        }
        UserDO userDO = userManage.login(param.getAccount());
        // 校验账号
        if (userDO == null) {
            throw new ApplicationException("用户名或者密码错误");
        }
        // 连续登录失败5次，当前时间 - 登录失败锁定时间 < 5分钟
        if (GlobalConstant.LOGIN_FAIL_COUNT.compareTo(userDO.getFailureCount()) <= 0 && Duration.between(userDO.getFailureFinishTime(), LocalDateTime.now()).toMillis() <= GlobalConstant.LOGIN_LOCK_TIME) {
            throw new ApplicationException("该账号超过" + GlobalConstant.LOGIN_FAIL_COUNT + "次登录失败，请" + GlobalConstant.LOGIN_LOCK_TIME_DESC + "后重新登录");
        }
        // 禁用
        if (DictConstant.DISABLE.equals(userDO.getStatus())) {
            throw new ApplicationException("该用户已被禁用，请联系管理员");
        }
        // 校验密码
        if (!DigestUtils.md5Hex(param.getPassword()).equals(userDO.getPassword())) {
            // 统计失败次数
            countFailure(userDO);
            throw new ApplicationException("用户名或者密码错误");
        }
        // 校验验证码
        if (authCode == null || !authCode.equals(param.getAuthCode())) {
            // 统计失败次数
            countFailure(userDO);
            throw new ApplicationException("验证码错误");
        }
        // 登录成功，失败次数为0
        if (0 != userDO.getFailureCount()) {
            userDO.setFailureCount(0);
            userManage.updateFailureCount(userDO);
        }
        CurrentUserDO currentUserDO = BeanCopierUtil.cloneObject(userDO, CurrentUserDO.class);
        // 首次登录
        if (DictConstant.DEFAULT.equals(userDO.getFirstLogin())) {
            // 不强制修改密码
            if (!GlobalConstant.FORCING_CHANGE_PASSWORD) {
                userManage.updateFirstLogin(userDO.getId());
            }
            currentUserDO.setFirstForce(GlobalConstant.FORCING_CHANGE_PASSWORD);
        }
        currentUserDO.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
        return currentUserDO;
    }

    /**
     * 统计失败次数
     *
     * @param userDO
     */
    private void countFailure(UserDO userDO) {
        if (0 == userDO.getFailureCount() || Duration.between(userDO.getFailureFirstTime(), LocalDateTime.now()).toMillis() > GlobalConstant.LOGIN_LOCK_TIME) {
            // 失败次数
            userDO.setFailureCount(1);
            // 首次登录失败时间
            userDO.setFailureFirstTime(LocalDateTime.now());
        } else {
            userDO.setFailureCount(userDO.getFailureCount() + 1);
        }
        // 登录失败次数等于5，设置登录失败锁定时间
        if (GlobalConstant.LOGIN_FAIL_COUNT <= userDO.getFailureCount()) {
            userDO.setFailureFinishTime(LocalDateTime.now());
        }
        userManage.updateFailureCount(userDO);
    }

    @Override
    public String authCode(OutputStream out) throws IOException {
        char[] randomSequence = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        // 在内存中创建图象
        int width = 60, height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        // 生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Times New Roman", Font.PLAIN, height - 2);
        // 设置字体。
        g.setFont(font);
        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        // 取随机产生的认证码(4位数字)
        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(9);
            String rand = String.valueOf(randomSequence[index]);
            sRand.append(rand);
            // 将认证码显示到图象中
            g.setColor(new Color(20 + random.nextInt(90), 20 + random.nextInt(100), 20 + random.nextInt(110)));
            // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.drawString(rand, 13 * i + 5, 25);
        }
        // 图象生效
        g.dispose();
        ImageIO.write(image, "JPEG", out);
        return sRand.toString();
    }

    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    @Override
    public ResponseDO<List<MenuDO>> menu(CurrentUserDO currentUserDO) {
        if (currentUserDO == null) {
            throw new ApplicationException("用户未登录");
        }
        List<MenuDO> menuDOList = menuManage.queryListByUserId(currentUserDO.getId());
        Set<String> menuUrlSet = menuDOList.stream().filter(item -> 3 == item.getLevel()).map(MenuDO::getUrl).collect(Collectors.toSet());
        // 菜单集合
        currentUserDO.setMenuUrlSet(menuUrlSet);
        // 去重
        menuDOList = menuDOList.stream()
                .collect(Collectors.groupingBy(MenuDO::getId))
                .values().stream()
                .map(value -> value.get(0))
                .collect(Collectors.toList());
        menuDOList = MenuUtil.getTree(menuDOList, -1L);
        menuDOList.sort(Comparator.comparing(MenuDO::getSort));
        // 权限集合
        Set<String> urlSet = menuUrlManage.queryListByUserId(currentUserDO.getId());
        currentUserDO.setUrlSet(urlSet);
        return ResponseDO.success(menuDOList);
    }
}
