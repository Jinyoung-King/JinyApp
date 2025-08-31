package com.jiny.jinyapp.repository;

import com.jiny.jinyapp.domain.Comment;
import com.jiny.jinyapp.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
