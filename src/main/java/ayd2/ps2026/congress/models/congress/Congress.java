package ayd2.ps2026.congress.models.congress;

import ayd2.ps2026.congress.common.models.entities.Auditor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Immutable
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
@EqualsAndHashCode(callSuper = true)
@Table(name = "congress", schema = "congress_ext_schema")
public class Congress extends Auditor {

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "date_init")
    private LocalDate dateInit;

    @Column
    private Double price;

}