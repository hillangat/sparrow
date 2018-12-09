package com.techmaster.sparrow.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContext;
import java.net.URL;
import java.util.Map;

public class SparrowBeanContext {

    private static ApplicationContext applicationContext;
    private static ServletContext servletContext;

    public static void setAppContext(ApplicationContext applicationContext) {
        SparrowBeanContext.applicationContext = applicationContext;
    }

    public static void setServletContext(ServletContext servletContext) {
        SparrowBeanContext.servletContext = servletContext;
    }

    public static String getRealPath(String path) {
        return servletContext.getRealPath(path);
    }

    public static URL getResource(String path) {
        return servletContext.getClassLoader().getResource(path);
    }

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
