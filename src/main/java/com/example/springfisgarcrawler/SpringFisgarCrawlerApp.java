package com.example.springfisgarcrawler;

import com.example.springfisgarcrawler.service.CookieService;
import com.example.springfisgarcrawler.service.CrawlerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringFisgarCrawlerApp implements CommandLineRunner {

    private final CookieService cookieService;
    private final CrawlerService crawlerService;

    public SpringFisgarCrawlerApp(CookieService cookieService, CrawlerService crawlerService) {
        this.cookieService = cookieService;
        this.crawlerService = crawlerService;
    }

    public static void main(String[] args) {
        SpringApplication
                .run(SpringFisgarCrawlerApp.class, args)
                .close();
    }

    @Override
    public void run(String... args) throws Exception {
        // Start setting the JSESSIONID auth cookie
        cookieService.setAuthCookie();
        // Then launches the rocket! 🚀🔥
        crawlerService.launch();
    }
}
