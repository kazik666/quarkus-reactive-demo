package com.example.rest.recaptcha.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class SiteVerifyResponse {
    private boolean success;
    private float score;
    private String action;
    @JsonProperty("challenge_ts")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
    private Date challengeTs;
    private String hostname;
    @JsonProperty("error-codes")
    private List<String> errorCodes;
}
