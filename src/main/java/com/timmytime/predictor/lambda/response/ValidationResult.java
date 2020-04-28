package com.timmytime.predictor.lambda.response;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
public class ValidationResult {

    private String key;
    private String result;

    public ValidationResult() {

    }


    public ValidationResult(String key, JSONObject result) {
        this.key = key;
        this.result = result.toString();
    }


}
