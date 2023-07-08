package com.example.springfisgarcrawler.config;

import com.example.springfisgarcrawler.context.CrawlerContext;
import com.example.springfisgarcrawler.utils.DateUtil;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;

@Configuration
public class OpenCsvConfig {

    public static final String FILENAME = "osasco-iptu"
            + "_"
            + LocalDateTime.now().format(DateUtil.toDashFormat())
            + ".csv";

    @Bean
    public Writer writer() throws IOException {
        return new FileWriter(FILENAME, true);
    }

    @Bean
    public StatefulBeanToCsv<CrawlerContext> beanToCsv(Writer fileWriter) {
        return new StatefulBeanToCsvBuilder<CrawlerContext>(fileWriter)
                .build();
    }

}
