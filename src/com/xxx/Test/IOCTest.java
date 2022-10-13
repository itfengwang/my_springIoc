package com.xxx.Test;

import com.xxx.IOCUtils.IOCParse;
import com.xxx.controller.UserController;
import com.xxx.factory.BeanFactory;

import java.util.Map;
import java.util.Set;

public class IOCTest {

    public static void main(String[] args) {
        //开启扫描包
        IOCParse.beginParseIOC("com.xxx");

        //获取表现层的实例
        UserController userServlet = (UserController)BeanFactory.getBean("UserController");

        userServlet.findUser();


    }
}
