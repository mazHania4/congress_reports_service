package ayd2.ps2026.congress.reports.repositories;

import ayd2.ps2026.congress.models.attendance.Attendance;
import ayd2.ps2026.congress.reports.projections.AttendanceByActivityProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttendanceReportRepository extends JpaRepository<Attendance, Long> {

    /**
     * Asistencia agrupada por actividad, con filtros opcionales por actividad,
     * salón y rango de fechas (sobre la hora de inicio de la actividad).
     *
     * @param activityId ID de la actividad (puede ser {@code null})
     * @param roomId ID del salón (puede ser {@code null})
     * @param from hora de inicio mínima de la actividad (puede ser {@code null})
     * @param to hora de inicio máxima de la actividad (puede ser {@code null})
     */
    @Query(value = """
            SELECT
                a.id AS activityId,
                a.nombre AS activityName,
                a.room_id AS roomId,
                a.hora_inicio AS startTime,
                a.hora_fin AS endTime,
                COUNT(att.id) AS totalAttendances
            FROM activity_ext_schema.activities a
            LEFT JOIN attendance_ext_schema.attendances att ON att.activity_id = a.id
            WHERE
                (:activityId IS NULL OR a.id = :activityId)
                AND (:roomId IS NULL OR a.room_id = :roomId)
                AND (CAST(:from AS timestamp) IS NULL OR a.hora_inicio >= CAST(:from AS timestamp))
                AND (CAST(:to AS timestamp) IS NULL OR a.hora_inicio <= CAST(:to AS timestamp))
            GROUP BY a.id, a.nombre, a.room_id, a.hora_inicio, a.hora_fin
            ORDER BY a.hora_inicio ASC
            """, nativeQuery = true)
    List<AttendanceByActivityProjection> findAttendanceByActivity(
            @Param("activityId") Long activityId,
            @Param("roomId") Long roomId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
