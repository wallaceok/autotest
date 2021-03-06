package com.xn.common.objectfactory;


import com.xn.common.util.StringUtil;

import java.lang.reflect.Type;


public class EnumFactory extends InstanceFactory {
    @Override
    protected Object create(Type type, Object value) {
        if (StringUtil.isEmpty(value)) return null;

        return Enum.valueOf((Class) type, value.toString());
    }

    @Override
    public boolean support(Type type) {
        if (type instanceof Class) {
            return ((Class) type).isEnum();
        }
        return false;
    }
}
