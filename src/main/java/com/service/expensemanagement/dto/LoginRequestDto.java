package com.service.expensemanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @JsonProperty("username")
    @NotNull(message = "User name cannot be blank")
    private String username;

    @JsonProperty("password")
    @NotNull(message = "Password cannot be blank")
    @Pattern(regexp = "^[0-9]{4}", message = "Invalid password")
    private String password;
}
