package com.example.sideproject_board.service;

import com.example.sideproject_board.domain.Comment;
import com.example.sideproject_board.domain.Post;
import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.dto.CommentRequest;
import com.example.sideproject_board.dto.CommentResponse;
import com.example.sideproject_board.repository.CommentRepository;
import com.example.sideproject_board.repository.PostRepository;
import com.example.sideproject_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /* 댓글 조회 */
    public List<CommentResponse> findAll(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다"));
        List<Comment> comments = post.getComments();
        return comments.stream().map(CommentResponse::new).collect(Collectors.toList());
    }

    /* 댓글 생성 */
    @Transactional
    public Long create(Long postId, String nickName, CommentRequest request) {
        User user = userRepository.findByNickname(nickName);
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다."));
        request.setUser(user);
        request.setPost(post);
        Comment comment = new Comment();
        comment.setId(request.getId());
        comment.setUser(request.getUser());
        comment.setPost(request.getPost());
        comment.setComment(request.getComment());
        comment.setCreatedDate(request.getCreatedDate());
        comment.setModifiedDate(request.getModifiedDate());
        commentRepository.save(comment);
        return comment.getId();
    }

    /* 댓글 업데이트 */
    @Transactional
    public void update(Long postId, Long commentId, CommentRequest request) {
        Comment comment = commentRepository.findByPostIdAndId(postId, commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        comment.update(request.getComment());
    }

    /* 댓글 삭제 */
    @Transactional
    public void delete(Long postId, Long commentId) {
        Comment comment = commentRepository.findByPostIdAndId(postId, commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }
}
