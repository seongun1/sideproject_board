package com.example.sideproject_board.dto;

import com.example.sideproject_board.domain.Post;
import com.example.sideproject_board.domain.User;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class CommentRequest {
    private Long id;
    private User user;
    private Post post;
    private String comment;
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
}
