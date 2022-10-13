package com.xxx.factory;

import java.util.HashMap;
import java.util.Map;

//存放我们的实例
public class BeanFactory {
    //指定容器，用于存放实例
    private static Map<String,Object> beanMap;

    static {
        beanMap = new HashMap<>();
    }


    public static Map<String, Object> getBeanMap() {
        return beanMap;
    }

    public static void setBeanMap(Map<String, Object> beanMap) {
        BeanFactory.beanMap = beanMap;
    }

    //根据对应的标识获取实例
    public static Object getBean(String beanID){

        return beanMap.get(beanID);

    }

}
