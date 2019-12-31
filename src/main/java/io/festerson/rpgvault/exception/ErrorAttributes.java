package io.festerson.rpgvault.exception;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;

import java.util.Map;

@Component
public class ErrorAttributes extends DefaultErrorAttributes {

    private final ReqTracer tracer;

    ErrorAttributes(ReqTracer tracer) {
        super(false);
        this.tracer = tracer;
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        final Throwable error = getError(request);
        //System.out.println("error=" + error.getClass().getName());
        final Map<String, Object> errorAttributes = super.getErrorAttributes(request, false);
        errorAttributes.put(ErrorAttribute.TRACE_ID.value, tracer.traceId());
        if (error instanceof ServerWebInputException){
            //System.out.printf("Caught an instance of: %s, err: %s", MethodArgumentNotValidException.class, error);
            final HttpStatus errorStatus = ((ServerWebInputException) error).getStatus();
            errorAttributes.replace(ErrorAttribute.STATUS.value, 400);
            errorAttributes.replace(ErrorAttribute.ERROR.value, errorStatus.getReasonPhrase());
            return errorAttributes;
        }
        for(String key : errorAttributes.keySet()) {
            System.out.println(key + "=" + errorAttributes.get(key));
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
