package com.timmytime.predictor.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmytime.predictor.lambda.factory.RedisConnector;
import com.timmytime.predictor.lambda.repo.PlayersResponse;
import com.timmytime.predictor.lambda.response.PlayerResponse;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeamPlayersHandler implements RequestHandler<Map<String, String>, List<PlayerResponse>> {

    private final RedissonClient redissonClient = RedisConnector.connect(System.getenv("AWS_REDIS_HOST"));

    @Override
    public List<PlayerResponse> handleRequest(Map<String, String> stringStringMap, Context context) {

        String team = stringStringMap.get("team");

        RSet<String> repo = redissonClient.getSet(team, new org.redisson.client.codec.StringCodec());

        PlayersResponse data
                = repo.stream().map(m -> {
            try {
                return new ObjectMapper().readValue(m, PlayersResponse.class);
            } catch (JsonProcessingException e) {
                context.getLogger().log(e.getMessage());
                return null;
            }
        }).findFirst().get();

        return data.getPlayerResponses();
    }
}
