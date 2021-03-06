package com.timmytime.predictor.lambda.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class PredictionOutcome implements Serializable {

    private String eventDate;
    private String home;
    private String away;

    private Boolean outcome;
    private String predictions;

    private String score;

}
