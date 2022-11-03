package com.unisguard.webapi.config.swagger;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ListVendorExtension;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zemel
 * @date 2021/12/16 14:27
 * @desc 接口按定义顺序排序
 */
public class SwaggerOperationPositionPlugin implements OperationBuilderPlugin {
    private static final Logger log = LoggerFactory.getLogger(SwaggerOperationPositionPlugin.class);

    @Override
    public boolean supports(DocumentationType documentationType) {
        return SwaggerPluginSupport.pluginDoesApply(documentationType);
    }

    @Override
    public void apply(OperationContext context) {
        //1.没有ApiOperation注解的直接返回
        Optional<ApiOperation> apiOperation = context.findAnnotation(ApiOperation.class);
        if (!apiOperation.isPresent()) {
            return;
        }
        ApiOperationSupport apiOperationSupport = context.findAnnotation(ApiOperationSupport.class).orElse(null);
        if (apiOperationSupport != null) {
            int order = apiOperationSupport.order();
            context.operationBuilder().position(order);
            //添加扩展参数：x-order（支持knife4j-spring-ui）
            context.operationBuilder().extensions(Collections.singletonList(new StringVendorExtension("x-order", order + "")));
            String[] includeParameters = apiOperationSupport.includeParameters();
            if (includeParameters.length > 0) {
                Map<String, Boolean> map = new LinkedHashMap<>();
                for (String includeParameter : includeParameters) {
                    map.put(includeParameter, true);
                }
                List<Map<String, Boolean>> maps = new ArrayList<>();
                maps.add(map);
                ListVendorExtension<Map<String, Boolean>> listVendorExtension = new ListVendorExtension<>("x-includeParameters", maps);
                List<VendorExtension> vendorExtensions = new ArrayList<>();
                vendorExtensions.add(listVendorExtension);
                context.operationBuilder().extensions(vendorExtensions);
            }
            String[] ignoreParameters = apiOperationSupport.ignoreParameters();
            if (ignoreParameters.length > 0) {
                Map<String, Boolean> map = new LinkedHashMap<>();
                for (String ignoreParameter : ignoreParameters) {
                    map.put(ignoreParameter, true);
                }
                List<Map<String, Boolean>> maps = new ArrayList<>();
                maps.add(map);
                ListVendorExtension<Map<String, Boolean>> listVendorExtension = new ListVendorExtension<>("x-ignoreParameters", maps);
                List<VendorExtension> vendorExtensions = new ArrayList<>();
                vendorExtensions.add(listVendorExtension);
                context.operationBuilder().extensions(vendorExtensions);
            }
            return;
        }
        //2.获取当前方法的位置，然后设置进position中
        try {
            Class<? extends OperationContext> operationContextClass = context.getClass();
            Field requestContextFiled = operationContextClass.getDeclaredField("requestContext");
            requestContextFiled.setAccessible(true);
            RequestMappingContext requestContext = (RequestMappingContext) requestContextFiled.get(context);

            Class<? extends RequestMappingContext> requestContextClass = requestContext.getClass();
            Field handler = requestContextClass.getDeclaredField("handler");
            handler.setAccessible(true);
            RequestHandler requestHandler = (RequestHandler) handler.get(requestContext);

            //得到当前handler对应的Controller
            Class<?> aClass = requestHandler.declaringClass();
            //获取所有方法
//            Method[] declaredMethods = aClass.getDeclaredMethods();
            //获取当前api对应哪个方法
            Method nowMethod = requestHandler.getHandlerMethod().getMethod();

            //等到当前方法在所有方法中的位置(TO:位置变成了编译后的位置，需要找到编译前的位置)
//            int indexOf = ArrayUtils.indexOf(declaredMethods, nowMethod);
            //使用javasisit获取到对应方法在原始类的多少行
            int indexOf = getMethodOriginalLine(aClass, nowMethod);
            if (indexOf != -1) {
                //swagger-ui高版本不支持position排序了，解决方法是引入knife4j-spring-ui
                //ps:找不到swagger-ui前端页面源代码加载位置，所以搞不定了~~~。有能力的小伙伴可以直接去修改swagger-ui的前端源代码
                context.operationBuilder().position(indexOf);
                //添加扩展参数：x-order（支持knife4j-spring-ui）
                context.operationBuilder().extensions(Collections.singletonList(new StringVendorExtension("x-order", indexOf + "")));
            }

        } catch (Exception e) {
            log.warn("加载swagger中方法api={}，设置顺序出错。", context.getName(), e);
        }
    }

    /**
     * 获取方法在类中的原始开始行数
     *
     * @param clazz     原始类
     * @param nowMethod 需要查找的哪个方法
     * @return
     */
    private int getMethodOriginalLine(Class clazz, Method nowMethod) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        //部署到tomcat需要添加下面两行代码，否则javassist.NotFoundException
        /**The default ClassPool returned by a static method ClassPool.getDefault() searches the same path that the underlying JVM (Java virtual machine) has. If a program is running on a web application server such as JBoss and Tomcat, the ClassPoolobject may not be able to find user classes since such a web application server uses multiple class loaders as well as the system class loader. In that case, an additional class path must be registered to the ClassPool.
         **/
        ClassClassPath classPath = new ClassClassPath(this.getClass());
        pool.insertClassPath(classPath);

        String className = clazz.getName();
        CtClass cc = pool.get(className);
        Class<?>[] parameterTypes = nowMethod.getParameterTypes();
        String[] objects = Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.toList()).toArray(new String[]{});
        return cc.getDeclaredMethod(nowMethod.getName(), pool.get(objects)).getMethodInfo().getLineNumber(0);
    }
}
