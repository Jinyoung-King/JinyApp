package com.jiny.jinyapp.service;

import com.jiny.jinyapp.domain.Comment;
import com.jiny.jinyapp.domain.Post;
import com.jiny.jinyapp.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> findCommentsByPost(Post post) {
        return commentRepository.findByPost(post);
    }

    public Comment addComment(Post post, String content) {
        Comment comment = Comment.builder()
                .post(post)
                .content(content)
                .build();
        return commentRepository.save(comment);
    }

    public Comment addComment(Post post, Comment comment) {
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
