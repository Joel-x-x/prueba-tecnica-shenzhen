package com.back.domain.audit;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Audit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Builder.Default
    @Column(name = "deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean deleted = false;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "datetime2(6)")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "datetime2(6)")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = false, columnDefinition = "datetime2(6)")
    private LocalDateTime deletedAt;
}
