package io.festerson.rpgvault.exception;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@Order(-2)
public class RpgVaultExceptionHandler extends AbstractErrorWebExceptionHandler {

    public RpgVaultExceptionHandler(RpgVaultErrorAttributes g, ApplicationContext applicationContext,
                                    ServerCodecConfigurer serverCodecConfigurer) {
        super(g, new ResourceProperties(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

        final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, false);

        if(errorPropertiesMap.containsKey("errors")) {
            StringBuilder errorMessages = new StringBuilder();
            List<FieldError> fieldErrors = (List) errorPropertiesMap.get("errors");
            fieldErrors.forEach(e -> errorMessages.append(e.getDefaultMessage() + " "));
            errorPropertiesMap.remove("errors");
            errorPropertiesMap.replace("message", errorMessages.toString());
        }

        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(errorPropertiesMap));
    }

}