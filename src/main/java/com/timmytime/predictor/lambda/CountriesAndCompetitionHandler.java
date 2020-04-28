package com.timmytime.predictor.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmytime.predictor.lambda.factory.RedisConnector;
import com.timmytime.predictor.lambda.repo.CountryAndCompetitionResponse;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CountriesAndCompetitionHandler implements RequestHandler<Map<String, String>, List<CountryAndCompetitionResponse>> {

    private final RedissonClient redissonClient = RedisConnector.connect(System.getenv("AWS_REDIS_HOST"));

    @Override
    public List<CountryAndCompetitionResponse> handleRequest(Map<String, String> stringStringMap, Context context) {


        RSet<String> countryResponses = redissonClient.getSet("CountryAndCompetitionResponse", new org.redisson.client.codec.StringCodec());

        return countryResponses
                .stream()
                .map(m -> {
                    try {
                        return new ObjectMapper().readValue(m, CountryAndCompetitionResponse.class);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                })
                .sorted(Comparator.comparing(o1 -> o1.getCountryResponse().getCountry()))
                .collect(Collectors.toList());
    }

}
