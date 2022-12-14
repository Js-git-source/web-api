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
     * ???????????????
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //1??????????????????convert?????????????????????
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2?????????fastjson???????????????????????????????????????????????????json?????????
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // ???????????????
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        // ????????????
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setSerializeFilters(new DictAfterFilter());
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat, //?????????????????????
                SerializerFeature.WriteMapNullValue, // ????????????null?????????,?????????false
                SerializerFeature.WriteNullStringAsEmpty, // ???????????????????????????null,?????????"",??????null
                SerializerFeature.WriteNullListAsEmpty, // List???????????????null,?????????[],??????null
                SerializerFeature.WriteNullBooleanAsFalse // Boolean???????????????null,?????????false,??????null
        );
        // ?????????????????????????????????????????????
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(mediaTypeList);
        //3??????convert?????????????????????
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //4??????convert?????????converters
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
