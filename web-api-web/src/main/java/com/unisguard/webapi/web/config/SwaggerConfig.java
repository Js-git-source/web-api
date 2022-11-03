package com.unisguard.webapi.web.config;

import com.unisguard.webapi.config.swagger.SwaggerOperationPositionPlugin;
import com.unisguard.webapi.config.swagger.SwaggerParameterPositionPlugin;
import com.unisguard.webapi.config.swagger.SwaggerRemoveBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author zemel
 * @date 2021/12/14 15:51
 */
@Profile({"sz-dev", "sz-test", "bj-dev", "bj-test"})
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("基础模块")
                .enable(true)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.unisguard.webapi.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("基础系统")
                //创建人
                // .contact(new Contact("zemel", "http://blog.bianxh.top/", ""))
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
    }

    @Bean
    public SwaggerRemoveBean swaggerRemoveBean() {
        return new SwaggerRemoveBean();
    }


    @Bean
    public SwaggerParameterPositionPlugin swaggerParameterPositionPlugin(DescriptionResolver descriptions, EnumTypeDeterminer enumTypeDeterminer) {
        return new SwaggerParameterPositionPlugin(descriptions, enumTypeDeterminer);
    }

    @Bean
    public SwaggerOperationPositionPlugin swaggerOperationPositionPlugin() {
        return new SwaggerOperationPositionPlugin();
    }
}
