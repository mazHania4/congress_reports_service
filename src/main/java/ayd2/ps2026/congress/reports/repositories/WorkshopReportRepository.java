package ayd2.ps2026.congress.reports.repositories;

import ayd2.ps2026.congress.models.registration.WorkshopReservation;
import ayd2.ps2026.congress.reports.projections.WorkshopParticipantProjection;
import ayd2.ps2026.congress.reports.projections.WorkshopSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkshopReportRepository extends JpaRepository<WorkshopReservation, Long> {

    /**
     * Resumen de reservas por taller (cupo total, reservas realizadas, cupos disponibles).
     * Si se proporciona {@code activityId} se devuelve solo ese taller.
     *
     * @param activityId ID del taller (puede ser {@code null})
     */
    @Query(value = """
            SELECT
                a.id AS activityId,
                a.nombre AS workshopName,
                a.cupo_max AS totalCapacity,
                COUNT(wr.reservation_id) AS totalReservations,
                (a.cupo_max - COUNT(wr.reservation_id)) AS availableSpots
            FROM activity_ext_schema.activities a
            LEFT JOIN registration_ext_schema.workshop_reservations wr ON wr.activity_id = a.id
            WHERE
                a.tipo = 'TALLER'
                AND (:activityId IS NULL OR a.id = :activityId)
            GROUP BY a.id, a.nombre, a.cupo_max
            ORDER BY a.nombre ASC
            """,
            nativeQuery = true)
    List<WorkshopSummaryProjection> findWorkshopSummary( @Param("activityId") Long activityId);

    /**
     * Listado de participantes con reserva en uno o todos los talleres.
     * Incluye su número de identificación, nombre completo, correo y rol.
     *
     * @param activityId ID del taller/actividad (puede ser {@code null})
     */
    @Query(value = """
            SELECT
                a.id AS activityId,
                a.nombre AS workshopName,
                u.identification AS identification,
                CONCAT(u.name, ' ', u.lastname) AS fullName,
                u.email AS email
            FROM registration_ext_schema.workshop_reservations wr
            JOIN activity_ext_schema.activities a ON a.id = wr.activity_id
            JOIN auth_ext_schema.app_user u ON u.id = wr.user_id
            WHERE
                a.tipo = 'TALLER'
                AND (:activityId IS NULL OR a.id = :activityId)
            ORDER BY a.nombre ASC, u.lastname ASC, u.name ASC
            """,
            nativeQuery = true)
    List<WorkshopParticipantProjection> findWorkshopParticipants(@Param("activityId") Long activityId);
}
