package ayd2.ps2026.congress.reports.services;

import ayd2.ps2026.congress.reports.projections.*;
import ayd2.ps2026.congress.reports.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock private RevenueReportRepository revenueReportRepository;
    @Mock private CongressReportRepository congressReportRepository;
    @Mock private ParticipantReportRepository participantReportRepository;
    @Mock private AttendanceReportRepository attendanceReportRepository;
    @Mock private WorkshopReportRepository workshopReportRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private final Long ID = 1L;
    private final LocalDateTime FROM_DATETIME = LocalDateTime.now().minusDays(5);
    private final LocalDateTime TO_DATETIME = LocalDateTime.now().plusDays(5);
    private final LocalDate FROM_DATE = LocalDate.now().minusDays(5);
    private final LocalDate TO_DATE = LocalDate.now().plusDays(5);

    @Test
    void getRevenueByInstitution_ShouldCallRepository() {
        // Arrange
        CongressRevenueProjection mockProjection = mock(CongressRevenueProjection.class);
        List<CongressRevenueProjection> expectedList = List.of(mockProjection);
        when(revenueReportRepository.findRevenueByInstitution(ID, FROM_DATETIME, TO_DATETIME)).thenReturn(expectedList);

        // Act
        List<CongressRevenueProjection> result = reportService.getRevenueByInstitution(ID, FROM_DATETIME, TO_DATETIME);

        // Assert
        assertThat(result).isEqualTo(expectedList);
        verify(revenueReportRepository).findRevenueByInstitution(ID, FROM_DATETIME, TO_DATETIME);
    }

    @Test
    void getRevenueTotals_ShouldCallRepository() {
        // Arrange
        RevenueSummaryProjection expectedProjection = mock(RevenueSummaryProjection.class);
        when(revenueReportRepository.findRevenueTotals(ID, FROM_DATETIME, TO_DATETIME)).thenReturn(expectedProjection);

        // Act
        RevenueSummaryProjection result = reportService.getRevenueTotals(ID, FROM_DATETIME, TO_DATETIME);

        // Assert
        assertThat(result).isEqualTo(expectedProjection);
        verify(revenueReportRepository).findRevenueTotals(ID, FROM_DATETIME, TO_DATETIME);
    }

    @Test
    void getCongressesByInstitution_ShouldCallRepository() {
        // Arrange
        CongressByInstitutionProjection mockProjection = mock(CongressByInstitutionProjection.class);
        List<CongressByInstitutionProjection> expectedList = List.of(mockProjection);
        when(congressReportRepository.findCongressesByInstitution(FROM_DATE, TO_DATE)).thenReturn(expectedList);

        // Act
        List<CongressByInstitutionProjection> result = reportService.getCongressesByInstitution(FROM_DATE, TO_DATE);

        // Assert
        assertThat(result).isEqualTo(expectedList);
        verify(congressReportRepository).findCongressesByInstitution(FROM_DATE, TO_DATE);
    }

    @Test
    void getParticipants_ShouldCallRepository() {
        // Arrange
        String role = "ADMIN";
        ParticipantReportProjection mockProjection = mock(ParticipantReportProjection.class);
        List<ParticipantReportProjection> expectedList = List.of(mockProjection);
        when(participantReportRepository.findParticipants(role)).thenReturn(expectedList);

        // Act
        List<ParticipantReportProjection> result = reportService.getParticipants(role);

        // Assert
        assertThat(result).isEqualTo(expectedList);
        verify(participantReportRepository).findParticipants(role);
    }

    @Test
    void getAttendanceByActivity_ShouldCallRepository() {
        // Arrange
        Long activityId = 10L;
        Long roomId = 20L;
        AttendanceByActivityProjection mockProjection = mock(AttendanceByActivityProjection.class);
        List<AttendanceByActivityProjection> expectedList = List.of(mockProjection);
        when(attendanceReportRepository.findAttendanceByActivity(activityId, roomId, FROM_DATETIME, TO_DATETIME))
                .thenReturn(expectedList);

        // Act
        List<AttendanceByActivityProjection> result = reportService.getAttendanceByActivity(activityId, roomId, FROM_DATETIME, TO_DATETIME);

        // Assert
        assertThat(result).isEqualTo(expectedList);
        verify(attendanceReportRepository).findAttendanceByActivity(activityId, roomId, FROM_DATETIME, TO_DATETIME);
    }

    @Test
    void getWorkshopSummary_ShouldCallRepository() {
        // Arrange
        WorkshopSummaryProjection mockProjection = mock(WorkshopSummaryProjection.class);
        List<WorkshopSummaryProjection> expectedList = List.of(mockProjection);
        when(workshopReportRepository.findWorkshopSummary(ID)).thenReturn(expectedList);

        // Act
        List<WorkshopSummaryProjection> result = reportService.getWorkshopSummary(ID);

        // Assert
        assertThat(result).isEqualTo(expectedList);
        verify(workshopReportRepository).findWorkshopSummary(ID);
    }

    @Test
    void getWorkshopParticipants_ShouldCallRepository() {
        // Arrange
        WorkshopParticipantProjection mockProjection = mock(WorkshopParticipantProjection.class);
        List<WorkshopParticipantProjection> expectedList = List.of(mockProjection);
        when(workshopReportRepository.findWorkshopParticipants(ID)).thenReturn(expectedList);

        // Act
        List<WorkshopParticipantProjection> result = reportService.getWorkshopParticipants(ID);

        // Assert
        assertThat(result).isEqualTo(expectedList);
        verify(workshopReportRepository).findWorkshopParticipants(ID);
    }

    @Test
    void getRevenueByCongress_ShouldCallRepository() {
        // Arrange
        CongressRevenueSummaryProjection mockProjection = mock(CongressRevenueSummaryProjection.class);
        List<CongressRevenueSummaryProjection> expectedList = List.of(mockProjection);
        when(revenueReportRepository.findRevenueByCongress(ID, FROM_DATE, TO_DATE)).thenReturn(expectedList);

        // Act
        List<CongressRevenueSummaryProjection> result = reportService.getRevenueByCongress(ID, FROM_DATE, TO_DATE);

        // Assert
        assertThat(result).isEqualTo(expectedList);
        verify(revenueReportRepository).findRevenueByCongress(ID, FROM_DATE, TO_DATE);
    }
}