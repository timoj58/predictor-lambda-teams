package com.timmytime.predictor.lambda.repo;

import com.timmytime.predictor.lambda.response.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CompetitionTeamsResponse implements Serializable {
    private String competition;
    private List<Team> teams = new ArrayList<>();

}
