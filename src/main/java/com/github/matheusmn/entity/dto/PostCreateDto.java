package com.github.matheusmn.entity.dto;

import com.github.matheusmn.entity.User;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PostCreateDto {

    @NotBlank
    private String text;
}
