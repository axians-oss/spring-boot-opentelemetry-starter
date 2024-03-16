package nl.axians.opentelemetry.springboot;

import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Default exception handler for the application which will handle unexpected exceptions.
 */
@Slf4j
@ControllerAdvice
@RequestMapping(produces = "application/json")
public class DefaultExceptionHandler {

    private static final String UNEXPECTED_EXCEPTION = """
            {
                "errorCode": "%s",
                "errorMessage": "%s"
            }""";

    private final Tracer tracer;

    /**
     * Create a new instance.
     *
     * @param tracer The tracer to use for tracing and adding trace and span identifiers.
     */
    public DefaultExceptionHandler(Tracer tracer) {
        this.tracer = tracer;

    }

    /**
     * Handle an unexpected exception.
     *
     * @param e The exception to handle.
     * @return A response entity with an error message and status code.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpectedException(Exception e) {
        try (final Tracer.SpanInScope ignored = tracer.withSpan(tracer.currentSpan())) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(UNEXPECTED_EXCEPTION.formatted(
                    e.getClass().getSimpleName(),
                    e.getMessage() != null ? e.getMessage() : ""),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
