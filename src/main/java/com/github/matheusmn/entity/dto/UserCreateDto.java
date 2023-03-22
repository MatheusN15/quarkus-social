package com.github.matheusmn.entity.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserCreateDto {

    @NotBlank(message = "Name is required")
    public String name;

    @NotNull(message = "Age is required")
    public Integer age;

    @NotBlank(message = "Email is required")
    public String email;
}
