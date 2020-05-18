package com.example.shortlink;

import com.example.shortlink.model.exception.UniqueShortLinkValidationException;
import com.example.shortlink.repository.LinkDatabaseRepository;
import com.example.shortlink.service.LinkDatabaseCache;
import com.example.shortlink.service.ShortLinkGenerator;
import com.example.shortlink.service.ShortLinkManagementService;
import com.example.shortlink.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {ShortLinkManagementService.class, LinkDatabaseCache.class, ShortLinkGenerator.class,
        ValidationService.class, LinkDatabaseRepository.class})
class ShortlinkServiceTests {

    private static final String ORIGINAL_URL = "http://www.google.com";
    private static final String CUSTOM_URL = "http://aaa.com";


    @Autowired
    private ShortLinkManagementService shortLinkManagementService;

    @Autowired
    private LinkDatabaseCache cache;

    @Autowired
    private LinkDatabaseRepository databaseRepository;

    @BeforeEach
    public void init() {
        cache.clear();
        databaseRepository.clear();
    }


    @Test
    void createLinkSimple() {
        String shortLink = shortLinkManagementService.createShortLink(ORIGINAL_URL);
        Assert.notNull(shortLink, "Short link should not be null");

        String originalLinkFromCache = cache.getOriginalLink(shortLink);
        Assert.isNull(originalLinkFromCache, "We do not put in cache on create, should have been null");

        String originalLinkFromDb = databaseRepository.getOriginalLink(shortLink);
        Assert.notNull(originalLinkFromDb, "result from DB should not be null");
        Assert.isTrue(ORIGINAL_URL.equals(originalLinkFromDb), "response short link should be equal to link from DB");
    }

    @Test
    void createCustom() {
        String shortLink = shortLinkManagementService.createShortLinkUsingCustomUrl(CUSTOM_URL, ORIGINAL_URL);
        Assert.notNull(shortLink, "Short link should not be null");

        String originalLinkFromCache = cache.getOriginalLink(shortLink);
        Assert.isNull(originalLinkFromCache, "We do not put in cache on create, should have been null");

        String originalLinkFromDb = databaseRepository.getOriginalLink(shortLink);
        Assert.notNull(originalLinkFromDb, "result from DB should not be null");
        Assert.isTrue(ORIGINAL_URL.equals(originalLinkFromDb), "response short link should be equal to link from DB");
    }

    @Test
    void createCustomAlreadyTakenInCache() {
        try {
            cache.addShortLink(CUSTOM_URL, ORIGINAL_URL);
            String shortLinkUsingCustomUrl = shortLinkManagementService.createShortLinkUsingCustomUrl(CUSTOM_URL, ORIGINAL_URL);
            Assert.isNull(shortLinkUsingCustomUrl, "shortLinkUsingCustomUrl should have thrown exception");
        } catch (Exception e) {
            Assert.isInstanceOf(UniqueShortLinkValidationException.class, e, "wrong exception type");
        }
    }

    @Test
    void createCustomAlreadyTakenInDb() {
        try {
            databaseRepository.addShortLink(CUSTOM_URL, ORIGINAL_URL);
            String shortLinkUsingCustomUrl = shortLinkManagementService.createShortLinkUsingCustomUrl(CUSTOM_URL, ORIGINAL_URL);
            Assert.isNull(shortLinkUsingCustomUrl, "shortLinkUsingCustomUrl should have thrown exception");
        } catch (Exception e) {
            Assert.isInstanceOf(UniqueShortLinkValidationException.class, e, "wrong exception type");
        }
    }

}
