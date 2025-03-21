package com.back.domain.user.entity;

import com.back.domain.audit.Audit;
import com.back.domain.role.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static java.time.LocalDateTime.now;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends Audit implements UserDetails {

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "first_names", nullable = false, length = 50)
    private String firstNames;

    @Column(name = "last_names", nullable = false, length = 50)
    private String lastNames;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "token", unique = true, length = 255)
    private String token;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<RoleEntity> roles;


    @PrePersist
    protected void onCreate() {
        this.setCreatedAt(now());
        this.setUpdatedAt(now());
        this.setDeletedAt(now());
        this.setDeleted(false);
        this.token = String.valueOf(new Random().nextInt(90000) + 10000);
    }

    @PreUpdate
    protected void onUpdate() {
        this.setUpdatedAt(now());
        if(this.getDeleted()) this.setDeletedAt(now());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add roles as GrantedAuthority
        this.roles.forEach(role -> {
            // Add role authority
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !this.getDeleted();
    }
}
