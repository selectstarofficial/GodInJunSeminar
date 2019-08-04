package com.example.demo;

import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestApplication {

    public static void main(String[] args) {
        int MAX_COUNTER = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        CyclicBarrier barrier = new CyclicBarrier(MAX_COUNTER );

        AtomicInteger integer = new AtomicInteger(0);

        String url = "http://localhost:8080/deffered?idx=";


        for(int i = 0; i < MAX_COUNTER; i++){
            executorService.submit(() -> {

                int idx = integer.addAndGet(1);

                try{
                    barrier.await();

                    StopWatch stopWatch = new StopWatch();

                    stopWatch.start();

                    RestTemplate restTemplate = new RestTemplate();

                    String forObject = restTemplate.getForObject(url + idx, String.class);

                    stopWatch.stop();

                    System.out.println(forObject + " ==> " + idx + " {result}: " + stopWatch.getTotalTimeSeconds());

                }catch (Exception e){

                }

            });
        }
    }
}
