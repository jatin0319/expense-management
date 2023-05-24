package com.service.expensemanagement.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ErrorDto {

    @JsonProperty("timeStamp")
    private String timeStamp;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

}
