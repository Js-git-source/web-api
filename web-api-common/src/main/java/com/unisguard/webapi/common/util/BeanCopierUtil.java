package com.unisguard.webapi.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * @author zemel
 * @date 2019/10/30 17:33
 */
public final class BeanCopierUtil {
    /**
     * 将一个对象转换为指定类型对象
     *
     * @param obj    源对象
     * @param tClass 目标类型
     * @param <T>
     * @return
     */
    public static <T> T cloneObject(Object obj, Class<T> tClass) {
        if (obj == null) {
            return null;
        }
        T target = BeanUtils.instantiateClass(tClass);
        BeanUtils.copyProperties(obj, target);
        return target;

    }

    /**
     * 将一个List转换为指定类型List
     *
     * @param obj    源对象
     * @param tClass 目标类型
     * @param <T>
     * @return
     */
    public static <T> List<T> cloneObject(List obj, Class<T> tClass) {
        if (null == obj) {
            return null;
        }
        List<T> list = new ArrayList<T>();
        Iterator iterator = obj.iterator();
        while (iterator.hasNext()) {
            T target = BeanUtils.instantiateClass(tClass);
            BeanUtils.copyProperties(iterator.next(), target);
            list.add(target);
        }
        return list;
    }

    /**
     * 复制非空属性
     *
     * @param src
     * @param target
     */
    public static void copyProperties(Object src, Object target) {
        String[] ignoreProperties = getNullOrEmptyProperties(src);
        copyProperties(src, target, ignoreProperties);
    }

    /**
     * 复制属性，忽略指定属性
     *
     * @param src
     * @param target
     * @param ignoreProperties
     */
    public static void copyProperties(Object src, Object target, String... ignoreProperties) {
        BeanUtils.copyProperties(src, target, ignoreProperties);
    }

    /**
     * 获取对象的空属性
     */
    private static String[] getNullOrEmptyProperties(Object src) {
        // 1.获取Bean
        BeanWrapper srcBean = new BeanWrapperImpl(src);
        // 2.获取Bean的属性描述
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        // 3.获取Bean的空属性
        Set<String> properties = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = srcBean.getPropertyValue(propertyName);
            if (StringUtils.isEmpty(propertyValue)) {
                properties.add(propertyName);
            }
        }
        return properties.toArray(new String[properties.size()]);
    }
}
