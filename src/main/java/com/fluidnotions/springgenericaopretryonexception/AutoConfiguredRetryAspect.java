package com.fluidnotions.springgenericaopretryonexception;

import org.springframework.context.annotation.Bean;

public class AutoConfiguredRetryAspect {
    @Bean
    public RetryUntilHelper retryUntilHelper() {
        return new RetryUntilHelper();
    }
}
