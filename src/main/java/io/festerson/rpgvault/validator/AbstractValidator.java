package io.festerson.rpgvault.validator;

import org.springframework.validation.Errors;

import java.util.function.Predicate;
import org.springframework.validation.Validator;

abstract class AbstractValidator implements Validator{

    private final int ABILITY_MAX = 20;
    private final int ABILITY_MIN = 3;
    private final String OUT_OF_RANGE = "field.out.of.range";

    private final Predicate<Integer> abilityLessThanMax = (x) -> x <=  ABILITY_MAX;
    private final Predicate<Integer> abilityMoreThanMin = (x) -> x >=  ABILITY_MIN;

    public void checkRange(Errors errors, String field, int target, int min, int max){
        if(! (target >= min && target <= max))
            errors.rejectValue(field, OUT_OF_RANGE, "Value must be from " + Integer.toString(min) + " to "+ Integer.toString(max));
    }

    private void checkMin(Errors errors, String field, int target, int min) {
        if(target < min)
            errors.rejectValue(field, OUT_OF_RANGE, "Value cannot be less than " + Integer.toString(min));
    }

    private void checkMax(Errors errors, String field, int target, int max) {
        if(target > max)
            errors.rejectValue(field, OUT_OF_RANGE, "Value cannot be more than " + Integer.toString(max));
    }

}
