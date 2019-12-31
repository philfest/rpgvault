package io.festerson.rpgvault.validator;

import io.festerson.rpgvault.domain.Campaign;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CampaignValidator extends AbstractValidator {

    public boolean supports(Class<?> clazz) {
        return Campaign.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", "The name field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dmId", "field.required", "The dmId field is required.");
        //Campaign request = (Campaign) target;
    }
}
