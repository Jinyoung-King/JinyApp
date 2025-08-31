package com.jiny.jinyapp.controller;

import com.jiny.jinyapp.domain.Comment;
import com.jiny.jinyapp.domain.Post;
import com.jiny.jinyapp.domain.User;
import com.jiny.jinyapp.service.CommentService;
import com.jiny.jinyapp.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    // 2025-06-22 댓글 등록자 처리로 인한 백업
//    @PostMapping
//    public String addComment(@PathVariable Long postId, @RequestParam String content) {
//        Post post = postService.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다. id=" + postId));
//        commentService.addComment(post, content);
//        return "redirect:/posts/" + postId;
//    }

    @PostMapping
    public String addComment(@PathVariable Long postId, @ModelAttribute Comment comment,
                             HttpServletRequest request,
                             HttpSession session) {
        log.debug("댓글을 등록합니다: {}", comment);
        Post post = postService.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다. id=" + postId));
        User loginUser = (User) session.getAttribute("loginUser");

        comment.setPost(post);

        if (loginUser != null) {
            comment.setUser(loginUser);
        } else {
            comment.setGuestNickname(comment.getGuestNickname());
        }

        log.debug("댓글이 등록되었습니다: {}", commentService.addComment(post, comment));
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }
}
