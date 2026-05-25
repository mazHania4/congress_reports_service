package ayd2.ps2026.congress.models.activity;

import ayd2.ps2026.congress.common.models.entities.SimpleAuditor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "rooms", schema = "activity_ext_schema")
public class Room extends SimpleAuditor {

    @Column
    private String nombre;

    @Column
    private Integer capacidad;

    @Column
    private String ubicacion;

}