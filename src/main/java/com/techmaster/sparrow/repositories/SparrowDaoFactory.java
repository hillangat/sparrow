package com.techmaster.sparrow.repositories;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

public class SparrowDaoFactory {


    public static ApplicationContext applicationContext;

    public static <T>T getObject(Class<T> clzz){
        return getDaoObject(clzz);
    }

    public static <T>T getDaoObject(Class<T> clzz){
        String key = getKeyForClass(clzz);
        Map<String, T> beanTypes = applicationContext.getBeansOfType(clzz);
        T t = beanTypes.get(key);
        t = t == null ? beanTypes.get(key + "Impl") : t;
        return t;
    }

    public static String getKeyForClass (Class<?> clzz) {
        String key = clzz.getSimpleName();
        key = (key.substring(0,1)).toLowerCase()+ key.substring(1,key.length());
        return key;
    }


}
