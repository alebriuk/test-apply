package com.challenge.salasia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {

    @Bean
    public RestClient algoliaRestClient() {
        return RestClient.builder()
                .baseUrl("https://hn.algolia.com/api/v1")
                .build();
    }
}
