package com.unisguard.webapi.config.filter;

import com.alibaba.fastjson.JSON;
import com.unisguard.webapi.common.constant.GlobalConstant;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.login.CurrentUserDO;
import com.unisguard.webapi.common.dataobject.login.UrlFilterDO;
import com.unisguard.webapi.common.exception.MessageCode;
import com.unisguard.webapi.common.util.ResourceUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 会话过滤器
 */
public class SessionFilter implements Filter {
    // 不登录可以访问的url
    private List<String> excludeList;
    // 登录后可以访问的url
    private List<String> authList;

    @Override

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();
        String url = request.getRequestURI();
        if ("/".equals(url) || "/login".equals(url) || "/lic".equals(url)) {
            request.getServletContext().getRequestDispatcher("/page/index.html").forward(request, response);
            return;
        }
        // 排除的url
        if (excludeList.stream().anyMatch(url::startsWith)) {
            chain.doFilter(request, response);
            return;
        }
        // 检查授权
        if (checkNotLic()) {
            // 前端静态路由和动态路由
            if (url.startsWith("/vue") || GlobalConstant.MENU_LIST.contains(url)) {
                request.getServletContext().getRequestDispatcher("/page/index.html").forward(request, response);
                return;
            }
            OutputStream outputStream = response.getOutputStream();
            response.setHeader("content-type", "application/json;charset=UTF-8");
            String result = JSON.toJSONString(ResponseDO.failure(MessageCode.UNLICENSED, "未授权"));
            outputStream.write(result.getBytes(StandardCharsets.UTF_8));
            return;
        }
        CurrentUserDO currentUserDO = (CurrentUserDO) session.getAttribute(GlobalConstant.CURRENT_USER);
        // 用户未登录或者失效
        if (currentUserDO == null) {
            // 前端静态路由和动态路由
            if (url.startsWith("/vue") || GlobalConstant.MENU_LIST.contains(url)) {
                request.getServletContext().getRequestDispatcher("/page/index.html").forward(request, response);
                return;
            }
            OutputStream outputStream = response.getOutputStream();
            response.setHeader("content-type", "application/json;charset=UTF-8");
            String result = JSON.toJSONString(ResponseDO.failure(MessageCode.LOGIN_EXPIRE, "登录过期"));
            outputStream.write(result.getBytes(StandardCharsets.UTF_8));
            return;
        }
        // 用户已经登录且不需要验证的url
        if (authList.stream().anyMatch(url::startsWith)) {
            chain.doFilter(request, response);
            return;
        }
        if (url.startsWith("/vue")) {
            request.getServletContext().getRequestDispatcher("/page/index.html").forward(request, response);
            return;
        }
        Set<String> menuUrlSet = currentUserDO.getMenuUrlSet();
        if (menuUrlSet.contains(url)) {
            request.getServletContext().getRequestDispatcher("/page/index.html").forward(request, response);
            return;
        }
        Set<String> urlSet = currentUserDO.getUrlSet();
        if (urlSet.contains(url)) {
            chain.doFilter(request, response);
            return;
        }
        OutputStream outputStream = response.getOutputStream();
        response.setHeader("content-type", "application/json;charset=UTF-8");
        String result = JSON.toJSONString(ResponseDO.failure("无访问权限"));
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 检查是否许可
     * 1. 打包需要把第一个换成true
     *
     * @return
     */
    private boolean checkNotLic() {
        if (GlobalConstant.LIC == null) {
            return false;
        }
        if (GlobalConstant.LIC.getAuthCycle() == -1) {
            return false;
        }
        return LocalDateTime.now().isAfter(GlobalConstant.LIC.getAuthTime().plusDays(GlobalConstant.LIC.getAuthCycle()));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String rootPath = ResourceUtil.getRootPath();
        if (StringUtils.isBlank(rootPath)) {
            return;
        }
        String path = rootPath + "/config/url-filter.json";
        try {
            InputStream inputStream = new FileInputStream(path);
            String json = IOUtils.toString(inputStream, Charset.defaultCharset());
            List<UrlFilterDO> urlFilterDOList = JSON.parseArray(json, UrlFilterDO.class);
            excludeList = getUrl("exclude", urlFilterDOList);
            authList = getUrl("auth", urlFilterDOList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getUrl(String exclude, List<UrlFilterDO> urlFilterDOList) {
        List<String> urlList = urlFilterDOList.stream().filter(item -> exclude.equals(item.getKey())).map(UrlFilterDO::getUrlList).findFirst().orElse(null);
        if (urlList == null) {
            urlList = new ArrayList<>();
        }
        return urlList;
    }

    @Override
    public void destroy() {

    }
}
