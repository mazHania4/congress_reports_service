package ayd2.ps2026.congress.reports.projections;

import java.time.LocalDate;

public interface CongressByInstitutionProjection {

    String getInstitutionName();

    String getInstitutionDescription();

    Long getCongressId();

    String getCongressName();

    String getCongressDescription();

    LocalDate getCongressDateInit();

    Double getCongressPrice();

}
