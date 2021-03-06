package com.timmytime.predictor.lambda.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class CountryResponse implements Serializable {

    private String country;

    public CountryResponse(String country) {
        this.country = country;
    }

}
