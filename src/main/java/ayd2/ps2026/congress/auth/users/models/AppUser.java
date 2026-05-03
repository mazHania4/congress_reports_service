package ayd2.ps2026.congress.auth.users.models;

import ayd2.ps2026.congress.auth.users.enums.RolesEnum;
import ayd2.ps2026.congress.common.models.entities.Auditor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Usuario interno de la aplicación
 * 
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class AppUser {

    private Integer id;

    private String username;

    private RolesEnum role;

}
