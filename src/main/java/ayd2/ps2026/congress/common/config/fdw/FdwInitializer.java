package ayd2.ps2026.congress.common.config.fdw;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FdwInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    private final FdwProperties fdwProperties;

    @Override
    public void run(String... args) throws Exception {
        log.info("----[  Iniciando Configuración Automatizada de FDW  ]----");

        jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS postgres_fdw;");
        for (FdwProperties.ExternalDatabase externalDatabase : fdwProperties.getExternal()) {
            try {
                configurarFdwParaServicio(externalDatabase);
            } catch (Exception e) {
                log.error("Error configurando FDW para: {}. Motivo: {}", externalDatabase.dbname(), e);
            }
        }

        System.out.println("----[  Configuración de FDW Finalizada  ]----");
    }

    private void configurarFdwParaServicio(FdwProperties.ExternalDatabase externalDatabase) {
        String serverName = externalDatabase.name() + "_server" ;
        String schemaName = externalDatabase.name() + "_ext_schema";

        log.info("Enlazando microservicio: [{}]...", externalDatabase.name().toUpperCase());
        jdbcTemplate.execute("DROP SERVER IF EXISTS " + serverName + " CASCADE;");

        String createServerSql = String.format(
                "CREATE SERVER %s FOREIGN DATA WRAPPER postgres_fdw OPTIONS (host '%s', dbname '%s', port '%d');",
                serverName, externalDatabase.host(), externalDatabase.dbname(), externalDatabase.port()
        );
        jdbcTemplate.execute(createServerSql);

        String createUserMappingSql = String.format(
                "CREATE USER MAPPING FOR current_user SERVER %s OPTIONS (user '%s', password '%s');",
                serverName, externalDatabase.username(), externalDatabase.password()
        );
        jdbcTemplate.execute(createUserMappingSql);

        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName + ";");

        String importSchemaSql = String.format(
                "IMPORT FOREIGN SCHEMA public FROM SERVER %s INTO %s;",
                serverName, schemaName
        );
        jdbcTemplate.execute(importSchemaSql);

        log.info("Listo! Tablas de [{}] importadas en el esquema: {} =)", externalDatabase.name(), schemaName);
    }
}
