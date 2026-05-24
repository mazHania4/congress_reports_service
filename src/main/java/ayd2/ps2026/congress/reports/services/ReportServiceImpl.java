package ayd2.ps2026.congress.reports.services;

import ayd2.ps2026.congress.reports.projections.*;
import ayd2.ps2026.congress.reports.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService{

    private final RevenueReportRepository revenueReportRepository;
    private final CongressReportRepository congressReportRepository;
    private final ParticipantReportRepository participantReportRepository;
    private final AttendanceReportRepository attendanceReportRepository;
    private final WorkshopReportRepository workshopReportRepository;

    @Override
    public List<CongressRevenueProjection> getRevenueByInstitution(Long institutionId, LocalDateTime from, LocalDateTime to) {
        return revenueReportRepository.findRevenueByInstitution(institutionId, from, to);
    }

    public RevenueSummaryProjection getRevenueTotals(Long institutionId, LocalDateTime from, LocalDateTime to) {
        return revenueReportRepository.findRevenueTotals(institutionId, from, to);
    }

    public List<CongressByInstitutionProjection> getCongressesByInstitution(LocalDate from, LocalDate to) {
        return congressReportRepository.findCongressesByInstitution(from, to);
    }

    public List<ParticipantReportProjection> getParticipants(String participantRole) {
        return participantReportRepository.findParticipants(participantRole);
    }

    public List<AttendanceByActivityProjection> getAttendanceByActivity(Long activityId, Long roomId, LocalDateTime from, LocalDateTime to) {
        return attendanceReportRepository.findAttendanceByActivity(activityId, roomId, from, to);
    }

    public List<WorkshopSummaryProjection> getWorkshopSummary(Long activityId) {
        return workshopReportRepository.findWorkshopSummary(activityId);
    }

    public List<WorkshopParticipantProjection> getWorkshopParticipants(Long activityId) {
        return workshopReportRepository.findWorkshopParticipants(activityId);
    }

    public List<CongressRevenueSummaryProjection> getRevenueByCongress(Long congressId, LocalDate from, LocalDate to) {
        return revenueReportRepository.findRevenueByCongress(congressId, from, to);
    }

}
