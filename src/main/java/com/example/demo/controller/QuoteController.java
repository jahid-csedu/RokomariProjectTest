package com.example.demo.controller;

import com.example.demo.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/quote")
public class QuoteController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping
    public Quote getquote() {
        return restTemplate.getForObject("https://api.quotable.io/random", Quote.class);
    }
}
