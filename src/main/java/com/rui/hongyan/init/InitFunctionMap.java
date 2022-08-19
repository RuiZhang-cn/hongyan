package com.rui.hongyan.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author by Rui
 * @Description
 * @Date 2022/07/29 上午 10:22
 */
@Component
public class InitFunctionMap implements BeanFactoryPostProcessor {


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        configurableListableBeanFactory.registerSingleton("configurableListableBeanFactory", configurableListableBeanFactory);
    }
}
