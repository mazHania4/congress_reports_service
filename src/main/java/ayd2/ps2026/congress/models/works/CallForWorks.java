package ayd2.ps2026.congress.models.works;

import ayd2.ps2026.congress.common.models.entities.Auditor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@DynamicUpdate
@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "calls_for_works", schema = "works_ext_schema")
public class CallForWorks extends Auditor {

    @Column
    private Long congressId;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private LocalDateTime openDate;

    @Column
    private LocalDateTime closeDate;

    @Column
    @Enumerated(EnumType.STRING)
    private CallStatus status;

    @Column
    private Long createdByUserId;

}
