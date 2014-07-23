package com.mitchz.apps.meimei.common.parser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表达式注解，用于标记域变量，标记哪个字段需要解析的方法
 *
 * Created by zhangya on 2014/7/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Expression
{
	String value();
	ExceptionType type() default ExceptionType.JSOUP;
	ExpressionParseType parseType() default  ExpressionParseType.Html;
}
