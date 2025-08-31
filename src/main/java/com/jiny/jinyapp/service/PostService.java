package com.jiny.jinyapp.service;

import com.jiny.jinyapp.domain.Post;
import com.jiny.jinyapp.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Post getPostWithViewIncrement(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다. id=" + id));
        post.setViewCount(post.getViewCount() + 1);
        return post;
    }

    @Transactional
    public void savePost(Post post, MultipartFile image) throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            // 폴더 없으면 생성
            log.info("{} 업로드 디렉터리가 존재하지 않아 생성하였습니다: {}", uploadDir.getAbsolutePath(), uploadDir.mkdirs());
        }
        if (image != null && !image.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            File file = new File(uploadPath, filename);
            image.transferTo(file);
            post.setImagePath("/images/" + filename);
        }
        postRepository.save(post);
    }

    @Transactional
    public void updatePost(Long id, String title, String content) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
//        if (!post.getAuthor().equals(loginUserId)) {
//            throw new IllegalStateException("게시글 작성자만 수정할 수 있습니다.");
//        }
        post.setTitle(title);
        post.setContent(content);
        post.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
//        if (!post.getAuthor().equals(loginUserId)) {
//            throw new IllegalStateException("게시글 작성자만 삭제할 수 있습니다.");
//        }
        postRepository.delete(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public Post createPost(String title, String content) {
        Post post = new Post(title, content);
        return postRepository.save(post);
    }

    @Transactional
    public List<Post> getTop5Posts() {
        return postRepository.findTop5ByOrderByViewCountDesc();
    }

    @Transactional
    public List<Post> getRecentPosts() {
        return postRepository.findTop5ByOrderByCreatedAtDesc();
    }

}
