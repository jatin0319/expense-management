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
public class CreateUserRequestDto {

    @NotNull(message = "Username cannot be null and unique")
    @Pattern(regexp = "^[A-Za-z0-9_]{3,20}", message = "Invalid userName")
    @JsonProperty("username")
    private String username;

    @NotNull(message = "Password cannot be null and unique 4 digit")
    @Pattern(regexp = "^[0-9]{4}", message = "Invalid password")
    @JsonProperty("password")
    private String password;

    @NotNull(message = "Firstname cannot be null")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{3,40}", message = "Invalid first name")
    @JsonProperty("firstName")
    private String firstName;

    @NotNull(message = "LastName cannot be null")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{3,40}", message = "Invalid last name")
    @JsonProperty("lastName")
    private String lastName;

}
