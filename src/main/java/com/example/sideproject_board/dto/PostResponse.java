package com.example.sideproject_board.dto;

import com.example.sideproject_board.domain.Post;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;
@Data
public class PostResponse {
    private Long postId;
    private Long memberId;
    private List<CommentResponse> comments;

    private String title;
    private String writer;
    private String content;
    private int view;
    private String createdDate, modifiedDate;

    public PostResponse(Post post) {
        this.postId = post.getId();
        this.memberId = post.getUser().getId();
        this.comments = post.getComments().stream().map(CommentResponse::new).collect(Collectors.toList());

        this.title = post.getTitle();
        this.writer = post.getWriter();
        this.content = post.getContent();
        this.view = post.getView();
        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();
    }
}
