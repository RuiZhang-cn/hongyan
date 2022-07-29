package com.rui.hongyan.init;

import cn.hutool.extra.spring.SpringUtil;
import com.rui.hongyan.function.HongYanBaseFunction;
import com.rui.hongyan.modules.hongyanmaptable.controller.HongyanController;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
