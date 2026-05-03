package ayd2.ps2026.congress.auth.users.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum que define los roles disponibles para los usuarios del sistema.
 * Incluye un nombre técnico y una etiqueta descriptiva.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-28-08
 */
@AllArgsConstructor
@Getter
public enum RolesEnum {

    ADMIN("Administrador")

    ;
    /**
     * Nombre descriptivo del rol para mostrar en interfaces de usuario.
     */
    private final String roleLabel;

}
