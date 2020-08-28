package br.com.luciano.multithread.services;

import br.com.luciano.multithread.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    @Autowired
    private ExternalApiService externalApiService;

    @Override
    public void run(String... args) {

        List<String> names = Arrays.asList("PivotalSoftware", "CloudFoundry", "Spring-Projects");

        long start = System.currentTimeMillis();

        List<CompletableFuture<User>> list = names.stream().map(s -> this.externalApiService.findUser(s))
                .collect(Collectors.toList());

        //Espera que todos terminem
        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();

        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));

        //Faz o processamento dos resultados pode usar CompletableFuture::get
        list.stream().map(CompletableFuture::join).forEach(s -> logger.info("Result: " + s));
    }
}
