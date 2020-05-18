package com.example.shortlink.service;

import com.example.shortlink.repository.LinkDatabaseRepository;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShortLinkManagementService {

    @Autowired
    private LinkDatabaseRepository databaseRepository;

    @Autowired
    private LinkDatabaseCache cache;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ShortLinkGenerator shortLinkGenerator;

    @Retryable
    public String createShortLink(String originalUrl) {
        String shortLink = shortLinkGenerator.generate();
        databaseRepository.addShortLink(shortLink, originalUrl);
        return shortLink;
    }

    public String createShortLinkUsingCustomUrl(String customShortLink, String originalUrl) {
        validationService.validateCustomShortLink(customShortLink);
        databaseRepository.addShortLink(customShortLink, originalUrl);
        return customShortLink;
    }

    public String getOriginalUrl(String shortLink) {
        String originalUrl = cache.getOriginalLink(shortLink);

        if (originalUrl != null) {
            return originalUrl;
        }

        log.debug("Could not find {} in cahce, going to DB", shortLink);

        originalUrl = databaseRepository.getOriginalLink(shortLink);
        if (!Strings.isNullOrEmpty(originalUrl)) {
            cache.addShortLink(shortLink, originalUrl);
        }

        return originalUrl;
    }
}
