package ayd2.ps2026.congress.models.activity;

import ayd2.ps2026.congress.common.models.entities.SimpleAuditor;
import ayd2.ps2026.congress.models.activity.enums.ActivityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Immutable
@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "activities", schema = "activity_ext_schema")
public class Activity extends SimpleAuditor {

    @Column
    private String nombre;

    @Column
    private String descripcion;

    @Column
    @Enumerated(EnumType.STRING)
    private ActivityType tipo;

    @Column
    private LocalDateTime horaInicio;

    @Column
    private LocalDateTime horaFin;

    @Column
    private Long congressId;

    @Column
    private Long roomId;

    @Column
    private Integer cupoMax;
}
