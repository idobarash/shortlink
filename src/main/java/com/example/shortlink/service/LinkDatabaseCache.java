package com.example.shortlink.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class LinkDatabaseCache {

    @Getter
    private Cache<String, String> shortLinkCache;

    @PostConstruct
    public void setup() {
        shortLinkCache = CacheBuilder.newBuilder()
                .maximumSize(1000000)
                .build();
    }


    public void addShortLink(String shortLink, String originalLink) {
        shortLinkCache.put(shortLink, originalLink);
        log.debug("Added {} --> {} to cache", shortLink, originalLink);
    }

    public String getOriginalLink(String shortLink) {
        return shortLinkCache.getIfPresent(shortLink);
    }

    public void clear() {
        shortLinkCache.invalidateAll();
    }
}
