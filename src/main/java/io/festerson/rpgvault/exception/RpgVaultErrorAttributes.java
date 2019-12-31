package io.festerson.rpgvault.exception;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;

import java.util.Map;

@Component
public class RpgVaultErrorAttributes extends DefaultErrorAttributes {

    private final ReqTracer tracer;

    RpgVaultErrorAttributes(ReqTracer tracer) {
        super(false);
        this.tracer = tracer;
    }

    // Integrates with functional endpoints on its own.
    // Annotaion-based endpoints have to get to here through RpgVaultExceptionHandler
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        final Throwable error = getError(request);
        final Map<String, Object> errorAttributes = super.getErrorAttributes(request, false);
        errorAttributes.put(ErrorAttribute.TRACE_ID.value, tracer.traceId());
        if (error instanceof ServerWebInputException){
            final HttpStatus errorStatus = ((ServerWebInputException) error).getStatus();
            errorAttributes.replace(ErrorAttribute.STATUS.value, 400);
            errorAttributes.replace(ErrorAttribute.ERROR.value, errorStatus.getReasonPhrase());
            return errorAttributes;
        }
        return errorAttributes;
    }

    enum ErrorAttribute {
        STATUS("status"),
        ERROR("error"),
        TRACE_ID("traceId");

        private final String value;

        ErrorAttribute(String value) {
            this.value = value;
        }
    }
}
