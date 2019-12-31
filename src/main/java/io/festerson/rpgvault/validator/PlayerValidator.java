package io.festerson.rpgvault.validator;

import io.festerson.rpgvault.domain.Player;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PlayerValidator  implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Player.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", "The name field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field.required", "The email field is required.");
        //Player request = (Player) target;
    }
}
