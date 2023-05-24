package com.service.expensemanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpenseListResponseDto {

    @JsonProperty("groupId")
    private Integer groupId;

    @JsonProperty("groupName")
    private String groupName;

    @JsonProperty("amount")
    private String amount;
}
