package ayd2.ps2026.congress.common.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

public class RestClientConfig {
    @Value("${app.microservice.url}")
    private String MICROSERVICE_NAME;

    @Bean
    public RestClient getMicroserviceNameRestClient() {
        return RestClient.builder()
                .baseUrl(MICROSERVICE_NAME)
                .build();
    }

}
