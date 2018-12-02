package com.techmaster.sparrow.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public class SparrowBeanContext {

    public static ApplicationContext applicationContext;

    public static <T>T getBean(Class<T> clzz){
        String key = getKeyForClass(clzz);
        Map<String, T> beanTypes = applicationContext.getBeansOfType(clzz);
        T t = beanTypes.get(key);
        t = t == null ? beanTypes.get(key + "Impl") : t;
        return t;
    }

    public static String getKeyForClass (Class<?> clzz) {
        String key = clzz.getSimpleName();
        key = (key.substring(0,1)).toLowerCase()+ key.substring(1);
        return key;
    }

    public static Session getSession() {
        SessionFactory sessionFactory = getBean(SessionFactory.class);
        if (sessionFactory != null)
            return sessionFactory.openSession();
        return null;
    }


}
