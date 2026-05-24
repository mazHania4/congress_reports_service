package ayd2.ps2026.congress;

import ayd2.ps2026.congress.common.config.env.EnvConfig;
import ayd2.ps2026.congress.common.config.fdw.FdwProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableConfigurationProperties(FdwProperties.class)
public class ReportsService {

	public static void main(String[] args) {
		new EnvConfig();
		SpringApplication.run(ReportsService.class, args);
	}

}
