package ayd2.ps2026.congress.reports.projections;

import java.time.LocalDate;

public interface CongressRevenueProjection {

    String getInstitutionName();

    Long getCongressId();

    String getCongressName();

    String getCongressDescription();

    LocalDate getCongressDateInit();

    Double getCongressPrice();

    Long getTotalRegistrations();

    Double getTotalCollected();

    Double getTotalCommission();

    Double getTotalProfit();

}
