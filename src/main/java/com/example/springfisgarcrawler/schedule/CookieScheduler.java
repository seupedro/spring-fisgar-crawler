package com.example.springfisgarcrawler.schedule;

import com.example.springfisgarcrawler.service.CookieService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CookieScheduler {

    private static final long TEN_MINUTES = 600000L;
    private final CookieService cookieService;

    public CookieScheduler(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @Scheduled(fixedRate = TEN_MINUTES)
    public void updateCookies() throws IOException, InterruptedException {
        cookieService.setAuthCookie();
    }
}
