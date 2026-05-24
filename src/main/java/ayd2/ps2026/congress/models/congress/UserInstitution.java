package ayd2.ps2026.congress.models.congress;

import ayd2.ps2026.congress.common.models.entities.Auditor;
import jakarta.persistence.*;
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
@Table(name = "user_institution", schema = "congress_ext_schema")
public class UserInstitution extends Auditor {

    @Column
    private Integer userId;

    @Column
    private String notes;

    @ManyToOne
    @JoinColumn
    private Institution institution;

}
