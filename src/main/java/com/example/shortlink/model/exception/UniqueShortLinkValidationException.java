package com.example.shortlink.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UniqueShortLinkValidationException extends RuntimeException {

    private String shortLink;

}
