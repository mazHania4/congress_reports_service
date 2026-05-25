package ayd2.ps2026.congress.reports.services;

import ayd2.ps2026.congress.reports.projections.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    /**
     * Devuelve el detalle de ganancias agrupado por institución y congreso.
     *
     * @param institutionId filtro opcional por institución
     * @param from inicio opcional del intervalo sobre la fecha de pago
     * @param to fin opcional del intervalo sobre la fecha de pago
     */
    List<CongressRevenueProjection> getRevenueByInstitution(Long institutionId, LocalDateTime from, LocalDateTime to);

    /**
     * Devuelve los totales globales del reporte de ganancias con los mismos filtros.
     */
    RevenueSummaryProjection getRevenueTotals(Long institutionId, LocalDateTime from, LocalDateTime to);

    /**
     * Devuelve los congresos agrupados por institución cuya fecha de inicio
     * esté dentro del intervalo indicado.
     *
     * @param from fecha de inicio mínima del congreso
     * @param to fecha de inicio máxima del congreso
     */
    List<CongressByInstitutionProjection> getCongressesByInstitution(LocalDate from, LocalDate to);

    /**
     * Devuelve el listado de participantes, opcionalmente filtrado por su rol.
     *
     * @param participantRole valor del enum {@code ParticipantRole} como String o {@code null} para todos
     */
    List<ParticipantReportProjection> getParticipants(String participantRole);

    /**
     * Devuelve la asistencia agrupada por actividad con filtros opcionales.
     *
     * @param activityId filtro opcional por actividad
     * @param roomId filtro opcional por salón
     * @param from hora de inicio mínima de la actividad
     * @param to hora de inicio máxima de la actividad
     */
    List<AttendanceByActivityProjection> getAttendanceByActivity(Long activityId, Long roomId, LocalDateTime from, LocalDateTime to);

    /**
     * Devuelve el resumen de cupos y reservas por taller.
     *
     * @param activityId filtro opcional por taller
     */
    List<WorkshopSummaryProjection> getWorkshopSummary(Long activityId);

    /**
     * Devuelve el listado de participantes con reserva en uno o todos los talleres.
     *
     * @param activityId filtro opcional por taller
     */
    List<WorkshopParticipantProjection> getWorkshopParticipants(Long activityId);

    /**
     * Devuelve el reporte de ganancias agrupado por congreso.
     *
     * @param congressId filtro opcional por congreso
     * @param from fecha de inicio mínima del congreso
     * @param to fecha de inicio máxima del congreso
     */
    List<CongressRevenueSummaryProjection> getRevenueByCongress(Long congressId, LocalDate from, LocalDate to) ;

}
