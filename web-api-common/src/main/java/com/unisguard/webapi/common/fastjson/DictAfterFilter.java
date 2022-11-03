package com.unisguard.webapi.common.fastjson;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.AfterFilter;
import com.unisguard.webapi.common.annotation.JSONDict;
import com.unisguard.webapi.common.dataobject.dict.DictDO;
import com.unisguard.webapi.common.util.DictUtil;
import lombok.SneakyThrows;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author zemel
 * @date 2022/1/5 9:49
 */
public class DictAfterFilter extends AfterFilter {

    @SneakyThrows
    @Override
    public void writeAfter(Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            return;
        }
        for (Field field : fields) {
            JSONDict jsonDict = field.getAnnotation(JSONDict.class);
            if (jsonDict == null) {
                continue;
            }
            int codeType = jsonDict.codeType();
            if (0 == codeType) {
                continue;
            }
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
            Method method = pd.getReadMethod();
            if (!method.getReturnType().equals(Integer.class)) {
                continue;
            }
            DictDO dictDO = DictUtil.getByCode(codeType, (Integer) method.invoke(object));
            if (dictDO == null) {
                continue;
            }
            writeKeyValue(field.getName() + "Name", dictDO.getName());
        }
    }
}
