package com.example.shortlink.controller;

import com.example.shortlink.model.ShortLinkCreateRequest;
import com.example.shortlink.model.exception.UniqueShortLinkValidationException;
import com.example.shortlink.service.ShortLinkManagementService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/shortlink")
public class ShortLinkController {

    @Autowired
    private ShortLinkManagementService shortLinkManagementService;

    @PostMapping
    public String createShortLink(@RequestBody @Validated ShortLinkCreateRequest request, HttpServletResponse response) throws IOException {
        try {

            if (request.getCustomShortLink() != null) {
                log.info("Got request to create short link using custom short link, custom: {}, original url: {}", request.getCustomShortLink(), request.getUrl());
                return shortLinkManagementService.createShortLinkUsingCustomUrl(request.getCustomShortLink(), request.getUrl());
            }

            log.info("Got request to create short link for original url: {}", request.getUrl());
            return shortLinkManagementService.createShortLink(request.getUrl());

        } catch (UniqueShortLinkValidationException e) {
            log.error("cannot create short link using custom link {} as it is already taken.");
            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());

            return null;
        }
    }

    @GetMapping
    public void getShortLink(@RequestParam("url") String shortLink, HttpServletResponse response) throws IOException {
        String originalUrl = shortLinkManagementService.getOriginalUrl(shortLink);
        if (!Strings.isNullOrEmpty(originalUrl)) {
            response.sendRedirect(originalUrl);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Could not find Short link " + shortLink);
        }
    }
}
