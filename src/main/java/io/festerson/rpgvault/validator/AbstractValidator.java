package io.festerson.rpgvault.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

abstract class AbstractValidator implements Validator{

    public final String BELOW_MIN = "field.min";
    public final String ABOVE_MAX = "field.max";
    public final String OUT_OF_RANGE = "field.range";
    public final String EMAIL_FORMAT = "field.email";

    public void checkRequired(Errors errors, List<String> fields){
        fields.forEach(name -> ValidationUtils.rejectIfEmptyOrWhitespace(
            errors,
            name,
            "field.required",
            "The " + name + " field is required."));
    }

    public void checkRange(Errors errors, String field, Integer target, Integer min, int max){
        if(target != null && (target < min || target > max))
            errors.rejectValue(field, OUT_OF_RANGE, "The field " + field + " must be in the range from " + Integer.toString(min) + " to "+ Integer.toString(max) + ".");
    }

    public void checkMin(Errors errors, String field, Integer target, int min) {
        if(target != null && target < min)
            errors.rejectValue(field, BELOW_MIN, "The field " + field + " cannot be less than " + Integer.toString(min) + ".");
    }

    public void checkMax(Errors errors, String field, Integer target, int max) {
        if(target != null && target > max)
            errors.rejectValue(field, ABOVE_MAX, "The field " + field + " cannot be more than " + Integer.toString(max) + ".");
    }

    public void checkEmail(Errors errors, String field, String target) {
        if(target != null && !target.contains("@")) {
            errors.rejectValue(field, EMAIL_FORMAT, "The field " + field + " must be a valid email address.");
        }
    }
}
