package com.jiny.jinyapp.repository;

import com.jiny.jinyapp.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);
    List<Post> findTop5ByOrderByViewCountDesc();     // 조회수 높은 순
    List<Post> findTop5ByOrderByCreatedAtDesc();     // 작성일 최신 순
}
