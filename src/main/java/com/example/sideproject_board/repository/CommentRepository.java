package com.example.sideproject_board.repository;

import com.example.sideproject_board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByPostIdAndId(Long postId, Long commentId);
}
