package com.timmytime.predictor.lambda.repo;

import com.timmytime.predictor.lambda.response.CompetitionResponse;
import com.timmytime.predictor.lambda.response.CountryResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CountryAndCompetitionResponse implements Serializable {

    private CountryResponse countryResponse;
    private List<CompetitionResponse> competitionResponses;
}
