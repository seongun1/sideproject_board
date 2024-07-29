package com.example.sideproject_board.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
public class Post extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String writer;

    private int view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    //== 게시글 업데이트 ==//
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //== 조회수 업데이트 ==//
    public void updateView() {
        this.view = this.view + 1;
    }
}