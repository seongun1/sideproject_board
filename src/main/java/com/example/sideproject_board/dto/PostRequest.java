package com.example.sideproject_board.dto;

import com.example.sideproject_board.domain.User;
import lombok.Data;

@Data
public class PostRequest {
    private Long id;
    private User user;

    private String title;
    private String writer;
    private String content;
    private int view;
    private String createdDate, lastModifiedDate;
}