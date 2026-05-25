package ayd2.ps2026.congress.reports.controllers;

import ayd2.ps2026.congress.reports.projections.*;
import ayd2.ps2026.congress.reports.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Endpoints para consultar de reportes del sistema")
public class ReportsController {

    private final ReportService reportService;

    @GetMapping("/revenues/institutions")
    @Operation(summary = "Reporte de ganancias por institución",
            description = "Devuelve el detalle de ganancias agrupado por institución y congreso. Permite filtrar por institución y rango de fechas de pago.")
    public List<CongressRevenueProjection> getRevenueByInstitution(
            @Parameter(description = "ID de la institución")
            @RequestParam(required = false)
            Long institutionId,

            @Parameter(description = "Fecha/hora mínima de pago")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @Parameter(description = "Fecha/hora máxima de pago")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) { return reportService.getRevenueByInstitution(institutionId, from, to); }

    @GetMapping("/revenues/institutions/summary")
    @Operation(summary = "Resumen global de ganancias",
            description = "Devuelve los totales globales del reporte de ganancias usando los mismos filtros del reporte por institución.")
    public RevenueSummaryProjection getRevenueTotals(
            @Parameter(description = "ID de la institución")
            @RequestParam(required = false)
            Long institutionId,

            @Parameter(description = "Fecha/hora mínima de pago")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @Parameter(description = "Fecha/hora máxima de pago")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) { return reportService.getRevenueTotals(institutionId, from, to); }

    @GetMapping("/congresses/institutions")
    @Operation(summary = "Congresos agrupados por institución",
            description = "Devuelve los congresos agrupados por institución cuya fecha de inicio esté dentro del intervalo indicado.")
    public List<CongressByInstitutionProjection> getCongressesByInstitution(
            @Parameter(description = "Fecha mínima de inicio del congreso")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @Parameter(description = "Fecha máxima de inicio del congreso")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    ) { return reportService.getCongressesByInstitution(from, to); }

    @GetMapping("/participants")
    @Operation(
            summary = "Reporte de participantes",
            description = "Devuelve el listado de participantes. Puede filtrarse por rol de participante.")
    public List<ParticipantReportProjection> getParticipants(
            @Parameter(description = "Rol del participante.") @RequestParam(required = false) String role
    ) { return reportService.getParticipants(role); }

    @GetMapping("/attendances/activities")
    @Operation(summary = "Reporte de asistencia por actividad",
            description = "Devuelve la asistencia agrupada por actividad. Permite filtros por actividad, salón y rango de fechas.")
    public List<AttendanceByActivityProjection> getAttendanceByActivity(
            @Parameter(description = "ID de la actividad")
            @RequestParam(required = false)
            Long activityId,

            @Parameter(description = "ID del salón")
            @RequestParam(required = false)
            Long roomId,

            @Parameter(description = "Fecha/hora mínima de inicio de actividad")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @Parameter(description = "Fecha/hora máxima de inicio de actividad")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) { return reportService.getAttendanceByActivity(activityId, roomId, from, to); }

    @GetMapping("/workshops/summary")
    @Operation(
            summary = "Resumen de talleres",
            description = "Devuelve el resumen de cupos y reservas de talleres. Puede filtrarse por actividad."
    )
    public List<WorkshopSummaryProjection> getWorkshopSummary(
            @Parameter(description = "ID del taller") @RequestParam(required = false) Long activityId
    ) { return reportService.getWorkshopSummary(activityId); }

    @GetMapping("/workshops/participants")
    @Operation(summary = "Participantes por taller",
            description = "Devuelve el listado de participantes registrados en talleres.")
    public List<WorkshopParticipantProjection> getWorkshopParticipants(
            @Parameter(description = "ID del taller") @RequestParam(required = false) Long activityId
    ) { return reportService.getWorkshopParticipants(activityId); }

    @GetMapping("/revenues/congresses")
    @Operation(summary = "Reporte de ganancias por congreso",
            description = "Devuelve el reporte de ganancias agrupado por congreso. Permite filtros por congreso y rango de fechas.")
    public List<CongressRevenueSummaryProjection> getRevenueByCongress(
            @Parameter(description = "ID del congreso")
            @RequestParam(required = false)
            Long congressId,

            @Parameter(description = "Fecha mínima de inicio del congreso")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @Parameter(description = "Fecha máxima de inicio del congreso")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    ) { return reportService.getRevenueByCongress(congressId, from, to); }
}