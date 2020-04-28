package com.timmytime.predictor.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmytime.predictor.lambda.factory.RedisConnector;
import com.timmytime.predictor.lambda.repo.PreviousFixtureResponse;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PreviousFixturesHandler implements RequestHandler<Map<String, String>, List<PreviousFixtureResponse>> {

    private final RedissonClient redissonClient = RedisConnector.connect(System.getenv("AWS_REDIS_HOST"));

    @Override
    public List<PreviousFixtureResponse> handleRequest(Map<String, String> stringStringMap, Context context) {

        LambdaLogger logger = context.getLogger();


        String competition = stringStringMap.get("competition");
        String market = stringStringMap.get("market");


        RSet<String> repo = redissonClient.getSet("PreviousFixtureResponse", new org.redisson.client.codec.StringCodec());

        return repo
                .stream()
                .map(m -> {

                    try {
                        return new ObjectMapper().readValue(m, PreviousFixtureResponse.class);
                    } catch (JsonProcessingException e) {
                        logger.log(e.getMessage());
                        return null;
                    }
                })
                .filter(f -> f.getHome().getCompetition().equals(competition))
                .collect(Collectors.toList());
    }
}
