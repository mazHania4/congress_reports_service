package ayd2.ps2026.congress.reports.repositories;

import ayd2.ps2026.congress.models.payments.Payment;
import ayd2.ps2026.congress.reports.projections.CongressRevenueProjection;
import ayd2.ps2026.congress.reports.projections.CongressRevenueSummaryProjection;
import ayd2.ps2026.congress.reports.projections.RevenueSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RevenueReportRepository extends JpaRepository<Payment, Long> {

    /**
     * Reporte de ganancias en un intervalo de tiempo, opcionalmente filtrado por institución.
     *
     * <p>Devuelve una fila por congreso, ordenadas alfabéticamente por institución
     * y de mayor a menor por ganancia dentro de cada institución.</p>
     *
     * @param institutionId ID de institución (puede ser {@code null})
     * @param from inicio del intervalo sobre {@code created_at} del pago (puede ser {@code null})
     * @param to fin del intervalo sobre {@code created_at} del pago (puede ser {@code null})
     */
    @Query(value = """
            SELECT
                i.name AS institutionName,
                c.id AS congressId,
                c.name AS congressName,
                c.description AS congressDescription,
                c.date_init AS congressDateInit,
                c.price AS congressPrice,
                COUNT(p.id) AS totalRegistrations,
                COALESCE(SUM(p.amount), 0) AS totalCollected,
                COALESCE(SUM(p.commission), 0) AS totalCommission,
                COALESCE(SUM(p.amount - p.commission), 0) AS totalProfit
            FROM payments_ext_schema.payment p
            JOIN congress_ext_schema.congress c ON c.id = p.congress_id
            JOIN congress_ext_schema.organizer o ON o.congress_id = c.id
            JOIN congress_ext_schema.user_institution ui ON ui.id = o.user_institution_id
            JOIN congress_ext_schema.institution i ON i.id = ui.institution_id
            WHERE
                (:institutionId IS NULL OR i.id = :institutionId)
                AND (CAST(:from AS timestamp) IS NULL OR p.created_at >= CAST(:from AS timestamp))
                AND (CAST(:to AS timestamp) IS NULL OR p.created_at <= CAST(:to AS timestamp))
            GROUP BY i.name, c.id, c.name, c.description, c.date_init, c.price
            ORDER BY i.name ASC, totalProfit DESC
            """,
            nativeQuery = true)
    List<CongressRevenueProjection> findRevenueByInstitution(
            @Param("institutionId") Long institutionId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    /**
     * Totales globales del reporte de ganancias (suma de todas las filas que
     * coincidan con los mismos filtros que {@link #findRevenueByInstitution}).
     */
    @Query(value = """
            SELECT
                COALESCE(SUM(p.amount), 0) AS grandTotalCollected,
                COALESCE(SUM(p.commission), 0) AS grandTotalCommission,
                COALESCE(SUM(p.amount - p.commission), 0) AS grandTotalProfit
            FROM payments_ext_schema.payment p
            JOIN congress_ext_schema.congress c ON c.id = p.congress_id
            JOIN congress_ext_schema.organizer o ON o.congress_id = c.id
            JOIN congress_ext_schema.user_institution ui ON ui.id = o.user_institution_id
            JOIN congress_ext_schema.institution i ON i.id = ui.institution_id
            WHERE
                (:institutionId IS NULL OR i.id = :institutionId)
                AND (CAST(:from AS timestamp) IS NULL OR p.created_at >= CAST(:from AS timestamp))
                AND (CAST(:to AS timestamp) IS NULL OR p.created_at <= CAST(:to AS timestamp))
            """,
            nativeQuery = true)
    RevenueSummaryProjection findRevenueTotals(
            @Param("institutionId") Long institutionId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    /**
     * Reporte de ganancias por congreso individual, opcionalmente filtrado por
     * congreso y/o rango de fecha de inicio del congreso.
     *
     * @param congressId ID del congreso (puede ser {@code null})
     * @param from fecha de inicio mínima del congreso (puede ser {@code null})
     * @param to fecha de inicio máxima del congreso (puede ser {@code null})
     */
    @Query(value = """
            SELECT
                c.id AS congressId,
                c.name AS congressName,
                c.description AS congressDescription,
                c.date_init AS congressDateInit,
                c.price AS congressPrice,
                COUNT(p.id) AS totalRegistrations,
                COALESCE(SUM(p.amount), 0) AS totalCollected,
                COALESCE(SUM(p.commission), 0) AS totalCommission,
                COALESCE(SUM(p.amount - p.commission), 0) AS totalProfit
            FROM payments_ext_schema.payment p
            JOIN congress_ext_schema.congress c ON c.id = p.congress_id
            WHERE
                (:congressId IS NULL OR c.id = :congressId)
                AND (CAST(:from AS timestamp) IS NULL OR c.date_init >= CAST(:from AS timestamp))
                AND (CAST(:to AS timestamp) IS NULL OR c.date_init <= CAST(:to AS timestamp))
            GROUP BY c.id, c.name, c.description, c.date_init, c.price
            ORDER BY c.date_init DESC
            """,
            nativeQuery = true)
    List<CongressRevenueSummaryProjection> findRevenueByCongress(
            @Param("congressId") Long congressId,
            @Param("from") java.time.LocalDate from,
            @Param("to") java.time.LocalDate to
    );
}
