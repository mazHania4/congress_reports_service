package ayd2.ps2026.congress.models.congress;

import ayd2.ps2026.congress.common.models.entities.Auditor;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "organizer", schema = "congress_ext_schema")
public class Organizer extends Auditor {

    @ManyToOne
    @JoinColumn
    private UserInstitution userInstitution;

    @ManyToOne
    @JoinColumn
    private Congress congress;

}
