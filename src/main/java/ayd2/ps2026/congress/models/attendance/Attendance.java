package ayd2.ps2026.congress.models.attendance;

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
@Table(name = "attendances", schema = "attendance_ext_schema")
public class Attendance extends Auditor {

    @Column
    private Long participantId;

    @Column
    private Long activityId;

}