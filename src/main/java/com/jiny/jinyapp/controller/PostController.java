package com.jiny.jinyapp.controller;

import com.jiny.jinyapp.domain.Comment;
import com.jiny.jinyapp.domain.Post;
import com.jiny.jinyapp.domain.User;
import com.jiny.jinyapp.service.CommentService;
import com.jiny.jinyapp.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    /**
     * 게시글 목록 조회 (정렬 기능 포함)
     *
     * @param page      페이지 번호 (기본값: 0)
     * @param size      페이지 크기 (기본값: 10)
     * @param sort      정렬 기준 필드명 (예: "createdAt", "views")
     * @param direction 정렬 방향 ("asc" 또는 "desc")
     * @param model     뷰에 전달할 데이터 모델
     * @return 게시글 목록 화면
     */
    @GetMapping
    public String listPosts(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(defaultValue = "createdAt") String sort, // 기본: 최신순
                            @RequestParam(defaultValue = "desc") String direction, // 기본: 내림차순
                            Model model) {
        log.debug("게시글 목록 조회: sort(정렬 기준): {} | direction(정렬 방향): {}", sort, direction);

        // 정렬 방향 설정
        Sort sortOption = direction.equalsIgnoreCase("asc")
                ? Sort.by(sort).ascending()
                : Sort.by(sort).descending();

        // 페이지 및 정렬 정보 설정
        Pageable pageable = PageRequest.of(page, size, sortOption);

        // 게시글 페이지 조회
        Page<Post> postPage = postService.getPosts(pageable);

        // 뷰 렌더링용 데이터 추가
        model.addAttribute("postPage", postPage);
        model.addAttribute("sort", sort);           // 현재 정렬 기준
        model.addAttribute("direction", direction);     // 현재 정렬 방향

        return "post/list"; // templates/post/list.html
    }


    /**
     * 게시글 상세 조회
     *
     * @param id    게시글 ID
     * @param model 모델
     * @return 게시글 상세 조회 화면
     */
    @GetMapping("/{id}")
    public String postDetail(@PathVariable Long id, Model model,
                             HttpSession session) {
        Post post = postService.getPostWithViewIncrement(id);

        if (post.isSecret()) {
            // 이미 세션에 인증된 글인지 체크
            Set<Long> unlockedPosts = (Set<Long>) session.getAttribute("unlockedPosts");
            if (unlockedPosts == null || !unlockedPosts.contains(id)) {
                model.addAttribute("postId", id);
                return "post/password-check";  // 비밀번호 확인 페이지로
            }
        }
        model.addAttribute("post", post);

        List<Comment> comments = commentService.findCommentsByPost(post);
        model.addAttribute("comments", comments);

        return "post/detail"; // detail.html 템플릿으로 연결
    }

    @PostMapping("/{id}/check-password")
    public String checkPassword(@PathVariable Long id, @RequestParam String password, HttpSession session, Model model) {
        Post post = postService.findById(id).orElseThrow();

        if (post.getPassword().equals(password)) {
            // 비밀번호 통과 → 세션에 저장
            Set<Long> unlockedPosts = (Set<Long>) session.getAttribute("unlockedPosts");
            if (unlockedPosts == null) {
                unlockedPosts = new HashSet<>();
            }
            unlockedPosts.add(id);
            session.setAttribute("unlockedPosts", unlockedPosts);
            return "redirect:/posts/" + id;
        } else {
            model.addAttribute("postId", id);
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "post/password-check";
        }
    }


    /**
     * 게시글 작성 버튼 클릭하여 작성 폼 화면 반환
     *
     * @param model 모델
     * @return 게시글 작성 폼 화면
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post()); // 빈 Post 객체 전달
        return "post/form"; // templates/post/form.html
    }

    // 게시글 저장 (폼 방식)
    @PostMapping("/form")
    public String savePost(@ModelAttribute Post post, // title, content
                           @RequestParam(required = false) MultipartFile image,
                           HttpSession session) throws IOException {
        log.debug("게시글을 저장합니다: {} | 파일: {}", post, image);

        User loginUser = (com.jiny.jinyapp.domain.User) session.getAttribute("loginUser");
        if (loginUser != null) {
            post.setAuthor(loginUser);
        }

        if (post.isSecret()) {
            post.setPassword(post.getPassword()); // 평문 저장
        }
        postService.savePost(post, image);
        return "redirect:/posts"; // 저장 후 목록으로 리다이렉트
    }

    // 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        Post post = postService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        com.jiny.jinyapp.domain.User loginUser = (com.jiny.jinyapp.domain.User) session.getAttribute("loginUser");
        if (loginUser == null || post.getAuthor() == null || !post.getAuthor().getId().equals(loginUser.getId())) {
            return "redirect:/posts/" + id + "?error=unauthorized";
        }

        model.addAttribute("post", post);
        return "post/edit";
    }

    // 수정 처리
    @PostMapping("/{id}/edit")
    public String updatePost(@PathVariable Long id,
                             @RequestParam String title,
                             @RequestParam String content,
                             HttpSession session
    ) {
        com.jiny.jinyapp.domain.User loginUser = (com.jiny.jinyapp.domain.User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/users/login";
        }

        postService.updatePost(id, title, content, loginUser.getId());
        return "redirect:/posts/" + id;
    }

    // 삭제 처리
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id, HttpSession session) {
        com.jiny.jinyapp.domain.User loginUser = (com.jiny.jinyapp.domain.User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/users/login";
        }

        postService.deletePost(id, loginUser.getId());
        return "redirect:/posts";
    }
}
