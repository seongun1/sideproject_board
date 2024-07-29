package com.example.sideproject_board.controller;

import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.dto.PostRequest;
import com.example.sideproject_board.dto.PostResponse;
import com.example.sideproject_board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    /* CREATE */
    @PostMapping("/posts")
    public ResponseEntity<Long> create(@RequestBody PostRequest request, User user) {
        Long createId = postService.create(request, user.getNickname());
        return ResponseEntity.ok(createId);
    }

    /* READ */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> read(@PathVariable Long postId) {
        PostResponse response = postService.read(postId);
        return ResponseEntity.ok(response);
    }

    /* UPDATE */
    @PutMapping("/posts/{postId}")
    public ResponseEntity<Void> update(@PathVariable Long postId, @RequestBody PostRequest request) {
        postService.update(postId, request);
        return ResponseEntity.ok().build();
    }

    /* DELETE */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok().build();
    }
}
