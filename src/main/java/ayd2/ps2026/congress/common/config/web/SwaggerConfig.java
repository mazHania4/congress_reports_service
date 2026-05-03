package ayd2.ps2026.congress.common.config.web;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para la documentación automática de la API
 * REST.
 *
 * Esta clase define la configuración necesaria para exponer la documentación de
 * la API utilizando Swagger UI a través de la especificación OpenAPI 3.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-05-31
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info().title("Congress Reports Service")
                        .version("1.0")
                        .description("""
    Documentación de la API para el servicio de Reportes"""
                        ));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public") // grupo de endpoints públicos
                .pathsToMatch("/api/**","/**") // se define el patrón de las rutas a documentar
                //.pathsToExclude("/api/hh/swagger*/**", "/api/hh/v3/api-docs*/**")
                .build();
    }
}
