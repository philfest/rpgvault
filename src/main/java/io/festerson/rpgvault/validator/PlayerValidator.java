package io.festerson.rpgvault.validator;

import io.festerson.rpgvault.domain.Player;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;

public class PlayerValidator  extends AbstractValidator {

    private static final List<String> REQUIRED_FIELDS = Arrays.asList("name", "email");

    @Override
    public boolean supports(Class<?> clazz) {
        return Player.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Player player = (Player) target;
        checkRequired(errors, REQUIRED_FIELDS);
        checkEmail(errors, "email", player.getEmail());
    }
}
