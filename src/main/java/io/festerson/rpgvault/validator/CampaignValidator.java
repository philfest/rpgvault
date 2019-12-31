package io.festerson.rpgvault.validator;

import io.festerson.rpgvault.domain.Campaign;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;

public class CampaignValidator extends AbstractValidator {

    private static final List<String> REQUIRED_FIELDS = Arrays.asList("name", "dmId");

    @Override
    public boolean supports(Class<?> clazz) {
        return Campaign.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //Campaign request = (Campaign) target;
        checkRequired(errors, REQUIRED_FIELDS);
    }
}
