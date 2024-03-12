package com.fluidnotions.springgenericaopretryonexception;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RetryUntil {
    RetryUntilType type() default RetryUntilType.NON_BLOCKING;
    int timeoutMinutes() default 10;
}

