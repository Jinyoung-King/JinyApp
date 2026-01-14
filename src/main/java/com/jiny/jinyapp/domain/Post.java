package com.jiny.jinyapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본 생성자를 public 접근제한자로 만듦 (기본값)
// @NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String imagePath; // 업로드된 이미지 경로

    @Column(nullable = false)
    private int viewCount = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ 댓글 연관관계 설정
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    // ✅ 작성자 연관관계 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    private boolean secret;  // true면 비밀글
    private String password;         // 비밀번호 (nullable)

    public Post(String title, String content) {
        log.debug("Creating new Post: {}, {}", title, content);
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        log.debug("Updating Post: {}, {}", title, content);
        this.content = content;
        this.title = title;
    }

    public void increaseViews() {
        log.debug("Increasing views: {}", viewCount);
        this.viewCount++;
    }
}
