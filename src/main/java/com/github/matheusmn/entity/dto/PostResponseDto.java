package com.github.matheusmn.entity.dto;

import com.github.matheusmn.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDto {

    private String text;
    private LocalDateTime dateTime;

    public static PostResponseDto fromEntity(Post post){
        PostResponseDto dto = new PostResponseDto();
        dto.setText(post.getText());
        dto.setDateTime(post.getDateTime());
        return dto;
    }
}
