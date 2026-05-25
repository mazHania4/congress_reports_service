package ayd2.ps2026.congress.models.payments;

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
@Table(name = "payment", schema = "payments_ext_schema")
public class Payment extends Auditor {

    @Column
    private Integer congressId;

    @Column
    private String description;

    @Column
    private Float amount;

    @Column
    private Float commission;

    @ManyToOne
    @JoinColumn
    private Wallet wallet;

}
