package com.example.shortlink.repository;

import com.example.shortlink.model.exception.UniqueShortLinkValidationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class LinkDatabaseRepository {

    @Getter
    private Map<String, String> db = new HashMap<>();

    public String addShortLink(String shortLink, String originalLink) {
        if (db.containsKey(shortLink)) {
            throw new UniqueShortLinkValidationException(shortLink);
        }

        db.put(shortLink, originalLink);

        log.debug("Added short link: {} --> {}", shortLink, originalLink);
        return shortLink;
    }

    public String getOriginalLink(String shortLink) {
        return db.get(shortLink);
    }

    public void clear() {
        db.clear();
    }
}
