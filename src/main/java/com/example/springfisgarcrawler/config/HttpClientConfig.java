package com.example.springfisgarcrawler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.http.HttpClient;
import java.util.Objects;

@Configuration
public class HttpClientConfig {



    @Bean()
    public HttpClient httpClient() {
        if (Objects.isNull(CookieHandler.getDefault())) {
            CookieHandler.setDefault(new CookieManager());
        }

        return HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .cookieHandler(CookieHandler.getDefault())
                .build();
    }
}
