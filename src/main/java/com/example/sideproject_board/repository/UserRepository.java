package com.example.sideproject_board.repository;

import com.example.sideproject_board.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNickname(String nickname);
    Optional<User> findByUsername (String username);

    Optional<User> findByEmail(String email);

    // 중복검사 -> 중복인 경우 true 않을경우 false
    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);
}
