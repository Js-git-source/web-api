package com.unisguard.webapi.config.swagger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zemel
 * @date 2021/12/19 18:11
 */
public class SwaggerRemoveBean implements BeanDefinitionRegistryPostProcessor {
    private List<String> list = new ArrayList<String>() {
        {
            // 实体类位置插件
            add("swaggerExpandedParameterBuilder");
        }
    };

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        list.forEach(beanName -> {
            if (beanDefinitionRegistry.containsBeanDefinition(beanName)) {
                beanDefinitionRegistry.removeBeanDefinition(beanName);
            }
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
