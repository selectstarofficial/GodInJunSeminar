package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class TestController {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor (){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setQueueCapacity(100);
        return threadPoolTaskExecutor;
    }

    private Queue<DeferredResult<String>> queue = new ConcurrentLinkedQueue<>();

    // DeferredResult
    @GetMapping("/test")
    public String test(Integer idx){
        try{
            Thread.sleep(1000);
        }catch (Exception e){ }

        return idx + " = ";
    }

    @GetMapping("/deffered")
    public DeferredResult<String> deferred(){
        DeferredResult deferredResult = new DeferredResult();
        ExecutorService executorService = Executors.newFixedThreadPool(200);
            // Stream IO
        List<String> list = new ArrayList();
        for(int idx = 0; idx < list.size(); idx++){
           String item = list.get(idx);
        }

        list.stream()
                .map(str -> {
            str += "added";
            return str;
        });

        CompletableFuture
                .completedFuture(executorService.submit(() -> {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "okok";
                })).thenApply(msg -> msg + " added_message")
                .thenAccept(msg -> deferredResult.setResult(msg));
        return deferredResult;
    }

    @GetMapping("/eventable")
    public ResponseBodyEmitter emitter(){
        ResponseBodyEmitter responseBodyEmitter = new ResponseBodyEmitter();

        for(int i = 0; i < 20; i++){
            try{
                Thread.sleep(500);
                responseBodyEmitter.send("<p> looool selectstart good luck ! </p>");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return responseBodyEmitter;
    }

    @GetMapping("/deffered/finish")
    public String finish(){
        for(DeferredResult<String> df : queue){
            df.setResult("YES");
        }
        return "";
    }

}
