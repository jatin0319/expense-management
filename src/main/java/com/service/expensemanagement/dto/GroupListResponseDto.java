package com.service.expensemanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupListResponseDto {

    @JsonProperty("groupId")
    private Integer groupId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;


}
