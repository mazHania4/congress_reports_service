package ayd2.ps2026.congress.models.works;

import ayd2.ps2026.congress.common.models.entities.Auditor;
import ayd2.ps2026.congress.models.activity.enums.ActivityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@DynamicUpdate
@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "proposed_works", schema = "works_ext_schema")
public class ProposedWorks extends Auditor {

    @ManyToOne
    @JoinColumn(name = "call_for_works_id")
    private CallForWorks callForWorks;

    @Column
    private Long participantId;

    @Column
    private String title;

    @Column
    private String summary;

    @Column
    @Enumerated(EnumType.STRING)
    private WorkType workType;

    @Column
    private String pathToDocument;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "file_content_type")
    private String fileContentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column
    @Enumerated(EnumType.STRING)
    private WorkStatus status;

    @Column
    private LocalDateTime submittedAt;

    @Column
    private LocalDateTime approvedAt;

    @Column
    private LocalDateTime rejectedAt;

    @Column
    private String rejectionReason;

}
