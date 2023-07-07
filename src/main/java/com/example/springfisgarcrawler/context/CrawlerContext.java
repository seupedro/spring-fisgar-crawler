package com.example.springfisgarcrawler.context;

import com.opencsv.bean.CsvIgnore;
import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Data
@Builder
public class CrawlerContext {

    private Integer iptuId;
    private Integer statusCode;
    private String responseBody;

    @CsvIgnore
    private URI uri;
    @CsvIgnore
    private String requestBody;
    @CsvIgnore
    private HttpRequest httpRequest;
    @CsvIgnore
    private HttpResponse<?> httpResponse;

    @Builder.Default
    private Boolean completed = false;

    @CsvIgnore
    @Builder.Default
    private Integer retries = 0;

    public void incrementRetries() {
        this.retries += 1;
    }

}
