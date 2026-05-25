package ayd2.ps2026.congress.reports.repositories;

import ayd2.ps2026.congress.models.activity.ActivityUser;
import ayd2.ps2026.congress.reports.projections.ParticipantReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantReportRepository extends JpaRepository<ActivityUser, Long> {

    /**
     * Listado general de participantes, opcionalmente filtrado por rol.
     *
     * <p>El parámetro {@code participantRole} debe coincidir con un valor del
     * enum {@code ParticipantRole}
     * Si es {@code null} se devuelven todos los roles.</p>
     *
     * @param participantRole rol del participante (puede ser {@code null})
     */
    @Query(value = """
            SELECT DISTINCT
                u.identification AS identification,
                CONCAT(u.name, ' ', u.lastname) AS fullName,
                COALESCE(i.name, '') AS organization,
                u.email AS email,
                u.cellphone AS cellphone,
                r.name AS participantRole
            FROM auth_ext_schema.app_user u 
            JOIN auth_ext_schema.app_user_role ur ON u.id = ur.app_user_id
            JOIN auth_ext_schema.role r ON r.id = ur.role_id
            LEFT JOIN congress_ext_schema.user_institution ui ON ui.user_id = u.id
            LEFT JOIN congress_ext_schema.institution i ON i.id = ui.institution_id
            WHERE
                (:participantRole IS NULL OR r.name = :participantRole)
            ORDER BY fullName ASC, participantRole ASC
            """,
            nativeQuery = true)
    List<ParticipantReportProjection> findParticipants(@Param("participantRole") String participantRole);
}
