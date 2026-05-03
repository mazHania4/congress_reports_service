package ayd2.ps2026.congress.common.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * Configuración global de zona horaria para la aplicación.
 * 
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-27
 */
@Configuration
public class TimeZoneConfig {

    /**
     * Establece la zona horaria por defecto en "America/Guatemala".
     * 
     * Este método se ejecuta automáticamente después de la construcción del
     * contexto de Spring y garantiza que cualquier operación con fechas utilice
     * la zona horaria especificada.
     */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guatemala"));
    }
}
