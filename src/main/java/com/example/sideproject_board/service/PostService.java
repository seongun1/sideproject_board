package com.example.sideproject_board.service;

import com.example.sideproject_board.domain.Post;
import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.dto.PostRequest;
import com.example.sideproject_board.dto.PostResponse;
import com.example.sideproject_board.repository.PostRepository;
import com.example.sideproject_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /* 게시글 조회 */
    public PostResponse read(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id"));
        return new PostResponse(post);
    }

    /* 게시글 생성 */
    @Transactional
    public Long create(PostRequest request, String nickname) {
        User user = userRepository.findByNickname(nickname);
        request.setUser(user);
        Post post = new Post();
        post.setId(request.getId());
        post.setUser(request.getUser());
        post.setTitle(request.getTitle());
        post.setWriter(request.getWriter());
        post.setContent(request.getContent());
        post.setView(request.getView());
        postRepository.save(post);
        return post.getId();
    }

    /* 게시글 업데이트 */
    @Transactional
    public void update(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id"));
        post.update(request.getTitle(), request.getContent());
    }

    /* 게시글 삭제 */
    @Transactional
    public void delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id"));
        postRepository.delete(post);
    }

    /* 페이징과 정렬 */
    public Page<Post> pageList(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    /* 게시글 검색 */
    public Page<Post> search(String keyword, Pageable pageable) {
        return postRepository.findByTitleContaining(keyword, pageable);
    }

    /* 조회수 */
    @Transactional
    public int updateView(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id"));
        post.updateView();
        return post.getView();
    }

}
