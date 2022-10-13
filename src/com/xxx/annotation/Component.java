package com.xxx.annotation;

import sun.security.krb5.internal.crypto.EType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //只能申明类
@Retention(RetentionPolicy.RUNTIME) //可以被反射机制所获取
public @interface Component {

}
