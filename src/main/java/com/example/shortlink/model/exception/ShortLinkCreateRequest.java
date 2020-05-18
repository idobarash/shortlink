package com.example.shortlink.model.exception;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkCreateRequest {

    @NonNull
    private String url;

    private String customShortLink;

}
