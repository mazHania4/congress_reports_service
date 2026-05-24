package ayd2.ps2026.congress.reports.projections;

import java.time.LocalDateTime;

public interface AttendanceByActivityProjection {

    Long getActivityId();

    String getActivityName();

    Long getRoomId();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    Long getTotalAttendances();

}
