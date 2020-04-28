package com.timmytime.predictor.lambda.repo;

import com.timmytime.predictor.lambda.response.ValidationResult;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TypeValidation {

    private String type;
    private List<ValidationResult> validations = new ArrayList<>();

    public TypeValidation() {

    }

    public TypeValidation(String type, List<ValidationResult> validations) {
        this.type = type;
        this.validations = validations;
    }

}
