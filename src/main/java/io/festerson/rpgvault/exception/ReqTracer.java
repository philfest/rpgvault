package io.festerson.rpgvault.exception;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ReqTracer {

    @Autowired
    private final Tracer tracer ;

    String traceId() {
        return tracer.currentSpan().context().traceIdString();
    }

}
