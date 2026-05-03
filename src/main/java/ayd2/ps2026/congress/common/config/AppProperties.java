package ayd2.ps2026.congress.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Guarda las propiedades de la aplicacion para su posterior uso
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-05-31
 */
@Getter
@Setter
@Configuration

public class AppProperties {

    @Value("${app.frontendHost}")
    private String frontendHost;

    @Value("${backend.host}")
    private String backendHost;

    @Value("${spring.mail.username}")
    private String mailFrom;
}
