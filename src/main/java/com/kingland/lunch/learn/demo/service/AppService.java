package com.kingland.lunch.learn.demo.service;


import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log
@AllArgsConstructor
@Service
public class AppService {
    private final @NonNull RestTemplate httpClient;

    @SneakyThrows
    public boolean waiting() {
        log.info("Start waiting");
        Thread.sleep(10000);
        log.info("Finish waiting");
        return true;
    }

    @Async("defaultCalcExecutor")
    public CompletableFuture<Long> cpuCalcTask(int n) {
        return CompletableFuture.completedFuture(uncachedFib(n));
    }


    @Async("defaultIOExecutor")
    public CompletableFuture<Integer> batchHttpCall(int delay, int times) {
        log.info(String.format("[%s] Launch tasks...", Thread.currentThread().getName()));
        List<CompletableFuture<Integer>> asyncTasks = IntStream.range(0, times)
                .mapToObj(id -> httpCallAsync(delay, id)).collect(Collectors.toList());

        // CompletableFuture::thenApply need an ExecuteContext so this method should carry @Async
        return CompletableFuture.allOf(asyncTasks.toArray(new CompletableFuture[0])).thenApply(v -> {
            log.info(String.format("[%s] Collecting results...", Thread.currentThread().getName()));
            Optional<Integer> result = asyncTasks.stream().map(CompletableFuture::join).reduce(Integer::sum);
            return result.orElse(0);
        });
    }

    @Async("defaultIOExecutor")
    CompletableFuture<Integer> httpCallSeq(int delay, int id) {
        String url = String.format("https://httpbin.org/delay/%s", delay);
        log.info(String.format("[%s] Start http call...", id));
        httpClient.getForEntity(url, Map.class);
        log.info(String.format("[%s] Finish http call...", id));
        return CompletableFuture.completedFuture(1);
    }
    
    @Async("defaultIOExecutor")
    CompletableFuture<Integer> httpCallAsync(int delay, int id) {
        return CompletableFuture.supplyAsync(() -> {
            String url = String.format("https://httpbin.org/delay/%s", delay);
            log.info(String.format("[%s] Start http call...", id));
            httpClient.getForEntity(url, Map.class);
            log.info(String.format("[%s] Finish http call...", id));
            return 1;
        });
    }

    /**
     * Fibonacci without cache
     *
     * @param n level
     * @return result
     */
    private long uncachedFib(long n) {
        if (n <= 1) {
            return n;
        }
        return uncachedFib(n - 1) + uncachedFib(n - 2);
    }

    /**
     * Fibonacci with cache
     *
     * @param n level
     * @return result
     */
    private long cachedFib(long n) {
        return cachedFib(n, new HashMap<>());
    }

    /**
     * Sub method for calc fibonacci with cache
     *
     * @param n     level
     * @param cache cache
     * @return result
     */
    private long cachedFib(long n, HashMap<Long, Long> cache) {
        if (n <= 1) {
            return n;
        }
        long i, j;
        if (cache.containsKey(n - 1)) {
            i = cache.get(n - 1);
        } else {
            i = cachedFib(n - 1, cache);
            cache.put(n - 1, i);
        }

        if (cache.containsKey(n - 2)) {
            j = cache.get(n - 2);
        } else {
            j = cachedFib(n - 2, cache);
            cache.put(n - 2, i);
        }
        return i + j;
    }

}
