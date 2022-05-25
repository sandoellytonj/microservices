package com.book.bookservice.controller;

import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("book-service")
public class FooBarController {

    private Logger logger = LoggerFactory.getLogger(FooBarController.class);

    @GetMapping("/foo-bar")
    @Retry(name = "foo-bar", fallbackMethod = "fallbackMethod")
    public String fooBar() {
        logger.info("Request to foo-bar is received");
        new RestTemplate()
                .getForEntity("http://localhost:8080/foo-bar", String.class);
        return "Foo-Bar!";
    }

    public String fallbackMethod(Exception exception) {
        return "fallbackMethod foo-bar";
    }
}
