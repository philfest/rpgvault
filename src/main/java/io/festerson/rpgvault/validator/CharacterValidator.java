package io.festerson.rpgvault.validator;

import io.festerson.rpgvault.domain.Character;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CharacterValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Character.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", "The name field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "crace", "field.required", "The crace field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cclass", "field.required", "The cclass field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "level", "field.required", "The level field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "strength", "field.required", "The strength field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "constitution", "field.required", "The constitution field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dexterity", "field.required", "The dexterity field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "intelligence", "field.required", "The intelligence field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "wisdom", "field.required", "The wisdom field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "charisma", "field.required", "The charisma field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ac", "field.required", "The ac field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "hp", "field.required", "The hp field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ctype", "field.required", "The ctype field is required.");
        //Character request = (Character) target;
    }
}
