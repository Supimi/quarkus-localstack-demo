package com.dgsp.demo.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse (
        @JsonProperty("success")
        boolean success,
        @JsonProperty("body")
        List<User> body
){

}
