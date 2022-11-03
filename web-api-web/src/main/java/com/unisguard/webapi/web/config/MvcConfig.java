package com.unisguard.webapi.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.unisguard.webapi.common.fastjson.DictAfterFilter;
import com.unisguard.webapi.config.filter.MultiReadFilter;
import com.unisguard.webapi.config.filter.SessionFilter;
import com.unisguard.webapi.config.listener.SessionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzemo
 * @date 2019/7/12 9:02
 */
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/page/**")
                .addResourceLocations("/page/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

    /**
     * 扩展转换器
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //1、先定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2、添加fastjson的配置信息，比如是否要格式化返回的json数据；
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 设置字符集
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        // 日期格式
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setSerializeFilters(new DictAfterFilter());
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat, //是否需要格式化
                SerializerFeature.WriteMapNullValue, // 输出值为null的字段,默认为false
                SerializerFeature.WriteNullStringAsEmpty, // 字符类型字段如果为null,输出为"",而非null
                SerializerFeature.WriteNullListAsEmpty, // List字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullBooleanAsFalse // Boolean字段如果为null,输出为false,而非null
        );
        // 附加：处理中文乱码（后期添加）
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(mediaTypeList);
        //3、在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //4、将convert添加到converters
        int size = converters.size();
        for (int i = 0; i < size; i++) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                converters.set(i, fastConverter);
                break;
            }
        }
    }

    @Bean
    public SessionFilter sessionFilter() {
        return new SessionFilter();
    }

    @Bean
    public MultiReadFilter multiReadFilter() {
        return new MultiReadFilter();
    }

    @Bean
    public SessionListener sessionListener() {
        return new SessionListener();
    }
}
