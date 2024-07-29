package com.example.sideproject_board.controller;

import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.dto.CommentRequest;
import com.example.sideproject_board.dto.CommentResponse;
import com.example.sideproject_board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentController {
    private final CommentService commentService;

    /* CREATE */
    @PostMapping("/posts/{commentId}/comments")
    public ResponseEntity<Long> create(@PathVariable Long commentId, @RequestBody CommentRequest request, User user) {
        Long createId = commentService.create(commentId, user.getNickname(), request);
        return ResponseEntity.ok(createId);
    }

    /* READ */
    @GetMapping("/posts/{commentId}/comments")
    public List<CommentResponse> read(@PathVariable Long commentId) {
        return commentService.findAll(commentId);
    }

    /* UPDATE */
    @PutMapping({"/posts/{postsId}/comments/{commentId}"})
    public ResponseEntity<Void> update(@PathVariable Long postsId, @PathVariable Long commentId, @RequestBody CommentRequest request) {
        commentService.update(postsId, commentId, request);
        return ResponseEntity.ok().build();
    }

    /* DELETE */
    @DeleteMapping("/posts/{postsId}/comments/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long postsId, @PathVariable Long commentId) {
        commentService.delete(postsId, commentId);
        return ResponseEntity.ok().build();
    }
}
