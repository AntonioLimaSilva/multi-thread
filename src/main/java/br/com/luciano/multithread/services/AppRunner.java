package br.com.luciano.multithread.services;

import br.com.luciano.multithread.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    @Autowired
    private ExternalApiService externalApiService;

    @Override
    public void run(String... args) {

        List<String> names = Arrays.asList("PivotalSoftware", "CloudFoundry", "Spring-Projects");

        long start = System.currentTimeMillis();

        List<CompletableFuture<User>> list = new ArrayList<>();
        names.forEach(s -> list.add(externalApiService.findUser(s)));

        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();

        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));

        list.forEach(f -> {
            try {
                logger.info("--> " + f.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
