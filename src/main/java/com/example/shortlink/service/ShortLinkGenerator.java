package com.example.shortlink.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ShortLinkGenerator {

    // Can be done with much smarter logic
    public String generate() {
        return Instant.now().toEpochMilli() + RandomStringUtils.randomAlphanumeric(10).toUpperCase();
    }
}
