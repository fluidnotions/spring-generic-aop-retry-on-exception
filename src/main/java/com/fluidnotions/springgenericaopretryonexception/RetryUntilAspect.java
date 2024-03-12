package com.fluidnotions.springgenericaopretryonexception;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RetryUntilAspect {

    private final RetryUntilHelper retryUntilHelper;

    @Around("@annotation(retryUntil)")
    public Object aroundExecution(ProceedingJoinPoint pjp, RetryUntil retryUntil) {
        return switch (retryUntil.type()) {
            case NON_BLOCKING -> retryUntilHelper.retryUntilSuccess(() -> {
                try {
                    return pjp.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }, false, retryUntil.timeoutMinutes());
            case BLOCKING -> retryUntilHelper.retryUntilSuccess(() -> {
                try {
                    return pjp.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }, true, retryUntil.timeoutMinutes());
            case TIMEOUT_NEVER -> retryUntilHelper.retryForeverUntilSuccess(() -> {
                try {
                    return pjp.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });
        };
    }
}
