package com.unisguard.webapi.config.swagger;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.readers.parameter.Examples;
import springfox.documentation.swagger.schema.ApiModelProperties;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zemel
 * @date 2021/12/17 13:51
 * @desc 参数排序
 */
public class SwaggerParameterPositionPlugin implements ExpandedParameterBuilderPlugin {
    private final DescriptionResolver descriptions;
    private final EnumTypeDeterminer enumTypeDeterminer;

    public SwaggerParameterPositionPlugin(DescriptionResolver descriptions, EnumTypeDeterminer enumTypeDeterminer) {
        this.descriptions = descriptions;
        this.enumTypeDeterminer = enumTypeDeterminer;
    }

    @Override
    public void apply(ParameterExpansionContext context) {
        context.findAnnotation(ApiModelProperty.class).ifPresent(apiModelProperty -> fromApiModelProperty(context, apiModelProperty));
        context.findAnnotation(ApiParam.class).ifPresent(apiParam -> fromApiParam(context, apiParam));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    private void fromApiParam(ParameterExpansionContext context, ApiParam apiParam) {
        String allowableProperty = apiParam.allowableValues();
        AllowableValues allowable = allowableValues(Optional.of(allowableProperty), context.getFieldType().getErasedType());
        maybeSetParameterName(context, apiParam.name())
                .description(descriptions.resolve(apiParam.value()))
                .defaultValue(apiParam.defaultValue())
                .required(apiParam.required())
                .allowMultiple(apiParam.allowMultiple())
                .allowableValues(allowable)
                .parameterAccess(apiParam.access())
                .hidden(apiParam.hidden())
                .scalarExample(apiParam.example())
                .complexExamples(Examples.examples(apiParam.examples()))
                .order(-2147482648)
                .build();
    }

    private void fromApiModelProperty(ParameterExpansionContext context, ApiModelProperty apiModelProperty) {
        String allowableProperty = apiModelProperty.allowableValues();
        AllowableValues allowable = allowableValues(Optional.of(allowableProperty), context.getFieldType().getErasedType());
        maybeSetParameterName(context, apiModelProperty.name())
                .description(descriptions.resolve(apiModelProperty.value()))
                .required(apiModelProperty.required())
                .allowableValues(allowable)
                .parameterAccess(apiModelProperty.access())
                .hidden(apiModelProperty.hidden())
                .scalarExample(apiModelProperty.example())
                .order(apiModelProperty.position()) //源码这里是: SWAGGER_PLUGIN_ORDER，需要修正
                .build();
    }

    private ParameterBuilder maybeSetParameterName(ParameterExpansionContext context, String parameterName) {
        if (StringUtils.hasText(parameterName)) {
            context.getParameterBuilder().name(parameterName);
        }
        return context.getParameterBuilder();
    }

    private AllowableValues allowableValues(final Optional<String> optionalAllowable, Class<?> fieldType) {
        AllowableValues allowable = null;
        if (this.enumTypeDeterminer.isEnum(fieldType)) {
            allowable = new AllowableListValues(this.getEnumValues(fieldType), "LIST");
        } else if (optionalAllowable.isPresent()) {
            allowable = ApiModelProperties.allowableValueFromString((String) optionalAllowable.get());
        }

        return allowable;
    }

    private List getEnumValues(final Class<?> subject) {
        return Stream.of(subject.getEnumConstants()).map(Object::toString).collect(Collectors.toList());
    }
}
