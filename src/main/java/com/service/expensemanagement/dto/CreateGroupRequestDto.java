package com.service.expensemanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupRequestDto {

    @JsonProperty("userIds")
    private List<Integer> userIds;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
}
