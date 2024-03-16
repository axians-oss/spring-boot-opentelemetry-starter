package nl.axians.opentelemetry.springboot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "management.otlp.logging.export")
public class AxiansOpenTelemetryConfiguration {

    /**
     * The endpoint for the OpenTelemetry log collector.
     */
    private String endpoint = "http://localhost:4317";

}
