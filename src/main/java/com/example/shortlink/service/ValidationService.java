package com.example.shortlink.service;

import com.example.shortlink.model.exception.UniqueShortLinkValidationException;
import com.example.shortlink.repository.LinkDatabaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidationService {

    @Autowired
    private LinkDatabaseRepository databaseRepository;

    @Autowired
    private LinkDatabaseCache cache;

    public void validateCustomShortLink(String customShortLink) {
        if (cache.getOriginalLink(customShortLink) != null || databaseRepository.getOriginalLink(customShortLink) != null) {
            throw new UniqueShortLinkValidationException("Short link " + customShortLink + " is already taken");
        }

        log.debug("Validated custom short link: {} is free for use ", customShortLink);
    }
}
