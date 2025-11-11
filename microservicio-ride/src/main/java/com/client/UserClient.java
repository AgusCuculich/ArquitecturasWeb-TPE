package com.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor

public class UserClient {
    private final RestTemplate restTemplate;

    public List<Long> getOtherUsers(Long userId) {
        String url = String.format("http://localhost:8088/users/accounts/%d", userId);
        Long[] response = restTemplate.getForObject(url, Long[].class);
        System.out.println(Arrays.toString(response));
        if (response == null || response.length == 0) return Collections.emptyList();
        return Arrays.asList(response);
    }

}