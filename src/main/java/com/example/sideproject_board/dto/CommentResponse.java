package com.example.sideproject_board.dto;

import com.example.sideproject_board.domain.Comment;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class CommentResponse {
    private Long commentId;
    private Long memberId;
    private Long postId;

    private String comment;
    private String nickName;
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));


    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.memberId = comment.getUser().getId();
        this.postId = comment.getPost().getId();

        this.comment = comment.getComment();
        this.nickName = comment.getUser().getNickname();
        this.createdDate = comment.getCreatedDate();
        this.modifiedDate = comment.getModifiedDate();
    }
}
