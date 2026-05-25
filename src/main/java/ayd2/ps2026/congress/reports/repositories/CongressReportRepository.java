package ayd2.ps2026.congress.reports.repositories;

import ayd2.ps2026.congress.models.congress.Congress;
import ayd2.ps2026.congress.reports.projections.CongressByInstitutionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CongressReportRepository extends JpaRepository<Congress, Long> {

    /**
     * Listado de congresos por institución cuya {@code date_init} esté dentro del
     * intervalo indicado. Ambos extremos del intervalo son opcionales.
     *
     * <p>Los resultados se ordenan alfabéticamente por institución y dentro de
     * cada institución por fecha de inicio ascendente.</p>
     *
     * @param from fecha de inicio mínima del congreso (puede ser {@code null})
     * @param to fecha de inicio máxima del congreso (puede ser {@code null})
     */
    @Query(value = """
            SELECT
                i.name AS institutionName,
                i.description AS institutionDescription,
                c.id AS congressId,
                c.name AS congressName,
                c.description AS congressDescription,
                c.date_init AS congressDateInit,
                c.price AS congressPrice
            FROM congress_ext_schema.congress c
            JOIN congress_ext_schema.organizer o ON o.congress_id = c.id
            JOIN congress_ext_schema.user_institution ui ON ui.id = o.user_institution_id
            JOIN congress_ext_schema.institution i ON i.id = ui.institution_id
            WHERE
                c.deleted_at IS NULL
                AND (CAST(:from AS timestamp) IS NULL OR c.date_init >= CAST(:from AS timestamp))
                AND (CAST(:to AS timestamp) IS NULL OR c.date_init <= CAST(:to AS timestamp))
            ORDER BY
                i.name ASC,
                c.date_init ASC
            """,
            nativeQuery = true)
    List<CongressByInstitutionProjection> findCongressesByInstitution(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
