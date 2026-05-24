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

@Entity
@Immutable
@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "institution", schema = "congress_ext_schema")
public class Institution extends Auditor {

    @Column
    private String name;

    @Column
    private String description;

}
