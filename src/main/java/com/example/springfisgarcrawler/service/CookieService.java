package com.example.springfisgarcrawler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class CookieService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HttpClient client;
    private static final HttpRequest COOKIE_REQUEST = HttpRequest.newBuilder()
            .uri(URI.create("http://www.sigt.osasco.sp.gov.br/iTRIB2/IptEmitirDAMDoIPTUExibir_Internet.do"))
            .GET()
            .timeout(Duration.ofSeconds(5L))
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    public CookieService(HttpClient client) {
        this.client = client;
    }

    public void setAuthCookie() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(COOKIE_REQUEST, HttpResponse.BodyHandlers.ofString());
        String jSessionIdCookie = response.headers()
                .map()
                .get("set-cookie")
                .stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No JSESSIONID cookie returned from the server. " +
                        "The application cannot proceed without the authentication cookie."));

        logger.info("JSESSION Cookie successfully retrived: " + jSessionIdCookie);
    }
}
