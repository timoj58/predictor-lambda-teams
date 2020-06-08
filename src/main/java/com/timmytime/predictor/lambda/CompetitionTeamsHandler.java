package com.timmytime.predictor.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmytime.predictor.lambda.factory.RedisConnector;
import com.timmytime.predictor.lambda.repo.CompetitionTeamsResponse;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompetitionTeamsHandler implements RequestHandler<Map<String, String>, CompetitionTeamsResponse> {

    private final RedissonClient redissonClient = RedisConnector.connect(System.getenv("AWS_REDIS_HOST"));

    @Override
    public CompetitionTeamsResponse handleRequest(Map<String, String> stringStringMap, Context context) {

        String competition = stringStringMap.get("competition");
        RSet<String> repo = redissonClient.getSet("CompetitionTeamsResponse", new org.redisson.client.codec.StringCodec());

        List<CompetitionTeamsResponse> data
                = repo.stream().map(m -> {
            try {
                return new ObjectMapper().readValue(m, CompetitionTeamsResponse.class);
            } catch (JsonProcessingException e) {
                return null;
            }
        }).collect(Collectors.toList());

        return data.stream().filter(f -> f.getCompetition().equals(competition)).findFirst().get();
    }
}
