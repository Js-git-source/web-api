package com.unisguard.webapi.config.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.unisguard.webapi.common.dataobject.base.ResponseDO;
import com.unisguard.webapi.common.dataobject.user.UserDO;
import com.unisguard.webapi.common.exception.ApplicationException;
import com.unisguard.webapi.config.wrapper.MultiReadHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zemel
 * @date 2021/5/27 10:41
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 日志模块
    private Map<Logger, List<Class<?>>> loggerMap = new HashMap<>();

    public Map<Logger, List<Class<?>>> getLoggerMap() {
        return loggerMap;
    }

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * 处理@Validated参数校验失败异常
     *
     * @param exception 异常类
     * @return 响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public ResponseDO bindExceptionHandler(BindException exception) {
        return getResultDO(exception.getBindingResult());
    }

    /**
     * 处理@Validated参数校验失败异常
     *
     * @param exception 异常类
     * @return 响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDO methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        return getResultDO(exception.getBindingResult());
    }

    private ResponseDO getResultDO(BindingResult bindingResult) {
        JSONObject jsonObject = new JSONObject();
        bindingResult.getFieldErrors().forEach(item -> jsonObject.put(item.getField(), item.getDefaultMessage()));
        return ResponseDO.failure(jsonObject.toJSONString());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = ApplicationException.class)
    public ResponseDO applicationExceptionHandler(ApplicationException ex) {
        return ResponseDO.failure(ex.getCode(), ex.getMessage(), ex.getData());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = Exception.class)
    public ResponseDO exceptionHandler(HttpServletRequest request, Exception ex) {
        // 格式化异常
        format(request, ex);
        return ResponseDO.failure("系统异常");
    }

    /**
     * 格式化异常
     *
     * @param request
     * @param ex
     */
    private void format(HttpServletRequest request, Exception ex) {
        StringBuilder sb = new StringBuilder();
        String split = "\n";
        // ip
        sb.append(split).append("ip:[").append(request.getRemoteHost()).append("]").append(split);
        // uri
        sb.append("uri:[").append(request.getRequestURI()).append("]").append(split);
        // 获取loggger和设置方法名称
        Logger logger = getLogger(request, sb, split);
        // 登录用户
        UserDO loginUser = (UserDO) request.getSession().getAttribute("loginUser");
        if (null != loginUser) {
            sb.append("loginUser:[").append(loginUser.getAccount()).append("]").append(split);
        }
        // 参数
        sb.append("param:[").append(formatParam(request)).append("]").append(split);
        if (logger == null) {
            log.error(sb.toString(), ex);
        } else {
            logger.error(sb.toString(), ex);
        }
    }

    /**
     * 获取loggger和设置方法名称
     *
     * @param request
     * @param sb
     * @param split
     * @return
     */
    private Logger getLogger(HttpServletRequest request, StringBuilder sb, String split) {
        HandlerExecutionChain handlerExecutionChain;
        try {
            handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
        } catch (Exception e) {
            log.warn("全局异常获取请求方法异常", e);
            return null;
        }
        if (handlerExecutionChain == null) {
            sb.append("method:[]").append(split);
            return null;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handlerExecutionChain.getHandler();
        sb.append("method:[").append(handlerMethod.getMethod().getName()).append("]").append(split);
        Map.Entry<Logger, List<Class<?>>> loggerEntry = loggerMap.entrySet().stream().filter(item -> item.getValue().contains(handlerMethod.getBeanType())).findFirst().orElse(null);
        return loggerEntry == null ? null : loggerEntry.getKey();
    }

    /**
     * 格式化参数
     *
     * @param request
     * @return
     */
    private String formatParam(HttpServletRequest request) {
        // 获取请求参数
        Map<String, String> map = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> map.put(k, String.join(",", v)));
        // 获取请求体参数
        String body = "";
        try {
            MultiReadHttpServletRequestWrapper multiReadHttpServletRequestWrapper = new MultiReadHttpServletRequestWrapper(request);
            body = multiReadHttpServletRequestWrapper.getBody();
        } catch (IOException e) {
            log.warn("全局异常获取请求体异常", e);
        }
        if (map.size() > 0) {
            body = body.equals("") ? "" : "," + body;
            body = JSON.toJSONString(map) + body;
        }
        return body;
    }
}
