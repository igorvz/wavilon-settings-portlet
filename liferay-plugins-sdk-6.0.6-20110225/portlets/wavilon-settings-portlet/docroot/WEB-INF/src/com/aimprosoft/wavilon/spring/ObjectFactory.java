package com.aimprosoft.wavilon.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

public class ObjectFactory {
    public static Object getBean(String beanName) {

        return getContext().getBean(beanName);
    }

    public static <T> T getBean(Class<T> beanClass) {

        return getContext().getBean(beanClass);
    }

    public static ApplicationContext getContext() {

        return ContextLoaderListener.getCurrentWebApplicationContext();
    }
}
