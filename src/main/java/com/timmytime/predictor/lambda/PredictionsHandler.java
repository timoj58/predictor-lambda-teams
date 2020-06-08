package com.timmytime.predictor.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmytime.predictor.lambda.factory.RedisConnector;
import com.timmytime.predictor.lambda.repo.EventOutcomeResponse;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PredictionsHandler implements RequestHandler<Map<String, String>, List<EventOutcomeResponse>> {

    private final RedissonClient redissonClient = RedisConnector.connect(System.getenv("AWS_REDIS_HOST"));

    @Override
    public List<EventOutcomeResponse> handleRequest(Map<String, String> stringStringMap, Context context) {

        UUID teamId = UUID.fromString(stringStringMap.get("team-id"));

        RSet<String> repo = redissonClient.getSet("EventOutcomeResponse_"+teamId.toString(), new org.redisson.client.codec.StringCodec());


         return repo
                .stream()
                .map(m -> {
                    try {

                        return new ObjectMapper().readValue(m, EventOutcomeResponse.class);
                    } catch (JsonProcessingException e) {

                        context.getLogger().log(e.getMessage());
                        return null;
                    }
                })
                .sorted(Comparator.comparing(EventOutcomeResponse::getEventDate))
                .collect(Collectors.toList());

       }
}
