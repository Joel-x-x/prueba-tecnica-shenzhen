package com.back.domain.role.entity;

import com.back.domain.audit.Audit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name="roles")
public class RoleEntity extends Audit {
    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

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
