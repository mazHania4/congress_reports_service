package ayd2.ps2026.congress.models.activity;

import ayd2.ps2026.congress.common.models.entities.SimpleAuditor;
import ayd2.ps2026.congress.models.activity.enums.ParticipantRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "activity_users", schema = "activity_ext_schema")
public class ActivityUser extends SimpleAuditor {

    @Column
    private Long activityId;

    @Column
    private Long userId;

    @Column
    @Enumerated(EnumType.STRING)
    private ParticipantRole role;

    @Column
    private LocalDateTime createdAt;

}