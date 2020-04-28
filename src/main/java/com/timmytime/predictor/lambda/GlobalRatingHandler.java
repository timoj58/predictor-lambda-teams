package com.timmytime.predictor.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmytime.predictor.lambda.factory.RedisConnector;
import com.timmytime.predictor.lambda.repo.TeamPerformanceGoalsResponse;
import com.timmytime.predictor.lambda.repo.TeamPerformanceResultsResponse;
import com.timmytime.predictor.lambda.response.TeamPerformanceResponse;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.UUID;

public class GlobalRatingHandler implements RequestHandler<Map<String, String>, TeamPerformanceResponse> {

    private final RedissonClient redissonClient = RedisConnector.connect(System.getenv("AWS_REDIS_HOST"));

    @Override
    public TeamPerformanceResponse handleRequest(Map<String, String> stringStringMap, Context context) {

        String market = stringStringMap.get("market");
        UUID teamId = UUID.fromString(stringStringMap.get("team"));


        if (market.equals("results")) {

            RSet<String> repo = redissonClient.getSet("TeamPerformanceResultsResponse", new org.redisson.client.codec.StringCodec());

            return repo
                    .stream()
                    .map(m -> {
                        try {
                            return new ObjectMapper().readValue(m, TeamPerformanceResultsResponse.class);
                        } catch (JsonProcessingException e) {
                            return null;
                        }
                    })
                    .filter(f -> f.getTeamId().equals(teamId))
                    .map(TeamPerformanceResponse::new)
                    .findFirst().get();

        } else {
            RSet<String> repo = redissonClient.getSet("TeamPerformanceGoalsResponse", new org.redisson.client.codec.StringCodec());

            return repo
                    .stream()
                    .map(m -> {
                        try {
                            return new ObjectMapper().readValue(m, TeamPerformanceGoalsResponse.class);
                        } catch (JsonProcessingException e) {
                            return null;
                        }
                    })
                    .filter(f -> f.getTeamId().equals(teamId))
                    .map(TeamPerformanceResponse::new)
                    .findFirst().get();
        }

    }
}
