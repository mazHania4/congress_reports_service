
package ayd2.ps2026.congress.common.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Representa a una persona en el sistema
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public abstract class Auditor {

    /**
     * Identificador único de la entidad. Se genera automáticamente utilizando
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Integer id;

    /**
     * Fecha y hora en que se creó el registro.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualización del registro.
     */
    @Column
    private LocalDateTime updatedAt;

    /**
     * Fecha y hora en que el registro fue eliminado lógicamente.
     */
    @Column
    private LocalDateTime deletedAt;

    /**
     * Fecha y hora en que el registro fue desactivado.
     */
    @Column
    private LocalDateTime deactivateAt;


}
