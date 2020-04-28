package com.timmytime.predictor.lambda.repo;

import com.timmytime.predictor.lambda.response.Bet;
import com.timmytime.predictor.lambda.response.Team;
import com.timmytime.predictor.lambda.response.TeamPredictionOutcome;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EventOutcomeResponse implements Serializable {


    private String eventType;

    private Team home;
    private Team away;

    private Date eventDate;

    private String predictions;

    private List<Bet> bets;

    private TeamPredictionOutcome homeOutcomes;
    private TeamPredictionOutcome awayOutcomes;
    private Double rating;
    private Integer successCount;
    private UUID eventOutcome;

}
