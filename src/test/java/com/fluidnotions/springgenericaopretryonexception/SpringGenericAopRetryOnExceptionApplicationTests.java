package com.fluidnotions.springgenericaopretryonexception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class SpringGenericAopRetryOnExceptionTests {

    final Logger logger = LoggerFactory.getLogger(SpringGenericAopRetryOnExceptionTests.class);

    @Autowired
    private RetryUntilHelper retryUntilHelper;

    @Test
    @Timeout(value = 4, unit = TimeUnit.MINUTES)
    void retryTestBlocking() {
        assertDoesNotThrow(() -> {
            long startTime = System.currentTimeMillis();
            AtomicInteger count = new AtomicInteger(0);
            var result = retryUntilHelper.retryUntilSuccess(() -> {
                try {
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int currentCount = count.getAndIncrement();
                logger.info("blocking current count: {}", currentCount);
                if (currentCount < 3) {
                    throw new RuntimeException("blocking Test Exception");
                }
                return "blocking success";
            }, true, 3);
            long endTime = System.currentTimeMillis();
            long durationInMillis = endTime - startTime;
            double durationInSeconds = durationInMillis / 1000.0;
            logger.info("Test took {} seconds. Result: {}", durationInSeconds, result);
        });
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.MINUTES)
    void retryTestNonBlocking() {
        assertDoesNotThrow(() -> {
            long startTime = System.currentTimeMillis();
            AtomicInteger count = new AtomicInteger(0);
            var assertNullResult = retryUntilHelper.retryUntilSuccess(() -> {

                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int currentCount = count.getAndIncrement();
                logger.info("non-blocking current count: {}", currentCount);
                if (currentCount < 3) {
                    throw new RuntimeException("non-blocking Test Exception");
                }
                return "non blocking success";
            }, false, 3);
            logger.info("Result: {} (should be null)", assertNullResult);
            try {
                TimeUnit.MINUTES.sleep(4);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            long endTime = System.currentTimeMillis();
            long durationInMillis = endTime - startTime;
            double durationInSeconds = durationInMillis / 1000.0;
            logger.info("Test took {} seconds", durationInSeconds);
        });
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.MINUTES)
    void retryTestNonBlockingFail() {
        assertDoesNotThrow(() -> {
            long startTime = System.currentTimeMillis();
            AtomicInteger count = new AtomicInteger(0);
            retryUntilHelper.retryUntilSuccess(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int currentCount = count.getAndIncrement();
                logger.info("non-blocking fail current count: {}", currentCount);
                throw new RuntimeException("non-blocking fail Test Exception");
            }, false, 3);
            try {
                TimeUnit.MINUTES.sleep(4);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            long endTime = System.currentTimeMillis();
            long durationInMillis = endTime - startTime;
            double durationInSeconds = durationInMillis / 1000.0;
            logger.info("Test took {} seconds", durationInSeconds);
        });
    }
}
