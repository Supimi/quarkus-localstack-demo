package com.dgsp.demo.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record User(
        @JsonProperty("user_id")
        String userId,
        @JsonProperty("first_name")
        String firstname,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("age")
        Integer age) {

}
