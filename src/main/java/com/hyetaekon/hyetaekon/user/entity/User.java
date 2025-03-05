package com.hyetaekon.hyetaekon.user.entity;

import com.hyetaekon.hyetaekon.UserInterest.entity.UserInterest;
import com.hyetaekon.hyetaekon.bookmark.entity.Bookmark;
import com.hyetaekon.hyetaekon.recommend.entity.Recommend;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "job", length = 50)
    private String job;

    @Column(name = "level", length = 20)
    private String level;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "delete_reason", columnDefinition = "TEXT")
    private String deleteReason;

    @Column(name = "suspend_start_at")
    private LocalDateTime suspendStartAt;

    @Column(name = "suspend_end_at")
    private LocalDateTime suspendEndAt;

    @Column(name = "suspend_reason", columnDefinition = "TEXT")
    private String suspendReason;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommend> recommends = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInterest> interests = new ArrayList<>();

    // 회원 탈퇴 로직
    public void deleteUser(String deleteReason) {
        this.deletedAt = LocalDateTime.now();
        this.deleteReason = deleteReason;
    }

    // 회원 닉네임 변경
    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }


    // 회원 비밀번호 변경
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
