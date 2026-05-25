package ayd2.ps2026.congress.common.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class SimpleAuditor {

    @Id
    private Long id;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

}