package com.kingland.lunch.learn.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Log
@AllArgsConstructor
@Service
@SuppressWarnings("DuplicatedCode")
public class DemoService {
    private final @NonNull AppService appService;

    public CompletableFuture<Integer> batchHttpCall(int delay, int times) {
        log.info(String.format("[%s] Launch tasks...", Thread.currentThread().getName()));
        List<CompletableFuture<Integer>> asyncTasks = IntStream.range(0, times)
                .mapToObj(id -> appService.httpCallSeq(delay, id)).collect(Collectors.toList());

        // CompletableFuture::thenApply need an ExecuteContext so this method should carry @Async
        return CompletableFuture.allOf(asyncTasks.toArray(new CompletableFuture[0])).thenApply(v -> {
            log.info(String.format("[%s] Collecting results...", Thread.currentThread().getName()));
            Optional<Integer> result = asyncTasks.stream().map(CompletableFuture::join).reduce(Integer::sum);
            return result.orElse(0);
        });
    }
}
