package com.service.expensemanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateExpenseRequestDto {

    @Digits(integer = 7, fraction = 2, message = "Total amount can be of more than 7 integer and 2 fraction")
    @Min(value = 1, message = "Total amount cannot be less than 1 rupee")
    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("groupId")
    private Integer groupId;
}
