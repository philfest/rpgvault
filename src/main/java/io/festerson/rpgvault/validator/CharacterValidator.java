package io.festerson.rpgvault.validator;

import io.festerson.rpgvault.domain.Character;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterValidator extends AbstractValidator {

    private static final List<String> REQUIRED_FIELDS =
        Arrays.asList(
            "name",
            "crace",
            "cclass",
            "level",
            "strength",
            "constitution",
            "dexterity",
            "intelligence",
            "wisdom",
            "charisma",
            "ac",
            "hp",
            "ctype",
            "playerId");

    @Override
    public boolean supports(Class<?> clazz) {
        return Character.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Character character = (Character) target;
        checkRequired(errors, REQUIRED_FIELDS);
        checkMin(errors, "strength", character.getStrength(), 3);
        checkMin(errors, "constitution", character.getConstitution(), 3);
        checkMin(errors, "dexterity", character.getDexterity(), 3);
        checkMin(errors, "intelligence", character.getIntelligence(), 3);
        checkMin(errors, "wisdom", character.getWisdom(), 3);
        checkMin(errors, "charisma", character.getCharisma(), 3);
        checkMin(errors, "level", character.getLevel(), 1);
        checkMin(errors, "ac", character.getAc(), 10);
        checkMin(errors, "hp", character.getHp(), 1);
    }
}
