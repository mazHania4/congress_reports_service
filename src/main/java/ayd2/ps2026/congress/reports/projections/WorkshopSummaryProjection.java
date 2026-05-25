package ayd2.ps2026.congress.reports.projections;

public interface WorkshopSummaryProjection {

    Long getActivityId();

    String getWorkshopName();

    Integer getTotalCapacity();

    Long getTotalReservations();

    Long getAvailableSpots();

}
