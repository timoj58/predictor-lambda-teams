package com.timmytime.predictor.lambda.response;

import com.timmytime.predictor.lambda.enumerator.Competition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class CompetitionResponse implements Serializable {

    private String competition;
    private String country;
    private String label;

    public CompetitionResponse(String country, Competition competition) {
        this.competition = competition.name();
        this.label = competition.getLabel();
        this.country = country;
    }

}
