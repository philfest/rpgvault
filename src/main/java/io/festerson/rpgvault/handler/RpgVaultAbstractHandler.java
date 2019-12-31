package io.festerson.rpgvault.handler;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;

abstract class RpgVaultAbstractHandler {

    public void manageValidationErrors(Errors errors){
        if (errors.hasErrors()) {
            List<FieldError> fieldErrors = errors.getFieldErrors();
            StringBuilder errorMessages = new StringBuilder();
            fieldErrors.forEach(e -> errorMessages.append(e.getDefaultMessage() + " "));
            throw new ServerWebInputException(errorMessages.toString());
        }
    }
}
