package ayd2.ps2026.congress.common.config.fdw;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "fdw")
public class FdwProperties {

    private List<ExternalDatabase> external;

    public record ExternalDatabase (
        String name,
        String host,
        Integer port,
        String dbname,
        String username,
        String password
    ) { }
}
