package com.fluidnotions.springgenericaopretryonexception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Slf4j
@Component
public class RetryUntilHelper {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public <T> T retryForeverUntilSuccess(Supplier<T> task) {
        return retryUntilSuccess(task,  false, Integer.MAX_VALUE);
    }

    public <T> T retryUntilSuccess(Supplier<T> task) {
        return retryUntilSuccess(task,  false, 10);
    }

    public <T> T retryUntilSuccess(Supplier<T> task, boolean block) {
        return retryUntilSuccess(task, block, 10);
    }

    public <T> T retryUntilSuccess(Supplier<T> task, boolean block, Integer minutesTimeout) {
        AtomicInteger count = new AtomicInteger(0);
        if(!block) {
            executorService.submit(() -> retry(task, System.currentTimeMillis(), minutesTimeout, count));
            return null;
        } else {
            return retry(task, System.currentTimeMillis(), minutesTimeout, count).join();
        }
    }

    private <T> CompletableFuture<T> retry(Supplier<T> task, long startTime, Integer minutesTimeout, AtomicInteger count) {
        if (System.currentTimeMillis() - startTime > (minutesTimeout * 60 * 1000)) {
            CompletableFuture<T> failedFuture = new CompletableFuture<>();
            failedFuture.complete(null);
            log.error("Retry time exceeded {} minutes", minutesTimeout);
            return failedFuture;
        }
        return CompletableFuture.supplyAsync(task).thenApply(t -> {
            log.info("Retried task completed successfully: result={}", t.toString());
            return t;
        }).exceptionally(e -> {
            log.warn("Retrying task failed: count={}, error={}", count.getAndIncrement(), e.getMessage());
            try {
                TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5, 16));//stagger load
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return retry(task, startTime, minutesTimeout, count).join();
        });
    }


}

