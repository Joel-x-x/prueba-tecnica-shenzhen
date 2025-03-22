package com.back.domain.post.entity;

import com.back.domain.audit.Audit;
import com.back.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static java.time.LocalDateTime.now;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="posts")
public class PostEntity extends Audit {
    @Column(name = "title", nullable = false, length = 50)
    private String title;
    @Column(name = "content", nullable = false, length = 1000)
    private String content;
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @PrePersist
    protected void onCreate() {
        this.setCreatedAt(now());
        this.setUpdatedAt(now());
        this.setDeletedAt(now());
        this.setDeleted(false);
    }

    @PreUpdate
    protected void onUpdate() {
        this.setUpdatedAt(now());
        if(this.getDeleted()) this.setDeletedAt(now());
    }
}
