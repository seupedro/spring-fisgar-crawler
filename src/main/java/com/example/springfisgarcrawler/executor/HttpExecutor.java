package com.example.springfisgarcrawler.executor;

import com.example.springfisgarcrawler.context.CrawlerContext;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Arrays;

@Component
public class HttpExecutor {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final HttpClient httpClient;
    private static final int MAX_RETRIES = 5;

    public HttpExecutor(java.net.http.HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @SneakyThrows
    public CrawlerContext toResponse(CrawlerContext context) {
        try {
            HttpResponse<String> response = httpClient.send(context.getHttpRequest(), HttpResponse.BodyHandlers.ofString());
            logger.info(String.format("[HTTP Client] POST executed for ID: %s, URL: %s, STATUS_CODE: %s, BODY: %s",
                    context.getIptuId(), context.getUri(), response.statusCode(), response.body()));

            context.setHttpResponse(response);
            context.setResponseBody(response.body());
            context.setStatusCode(response.statusCode());
            context.setCompleted(true);

        } catch (IOException | InterruptedException e) {
            this.handleException(context, e);
        }
        return context;
    }

    private void handleException(CrawlerContext context, Exception e) {
        if (context.getRetries() >= MAX_RETRIES) {
            logger.error(String.format("[HTTP Client] Failed definitively to execute for ID: %s, Message: %s, Stacktrace: %s",
                    context.getIptuId(), e.getMessage(), Arrays.toString(e.getStackTrace())));
            return;
        }

        logger.warn(String.format("[HTTP Client] [Retry: %s] Failed to execute for ID: %s, Message: %s, Stacktrace: %s",
                context.getRetries() + 1, context.getIptuId(), e.getMessage(), Arrays.toString(e.getStackTrace())));

        context.setCompleted(false);
        context.incrementRetries();
        this.toResponse(context);
    }
}
