package com.example.springfisgarcrawler.service;

import com.example.springfisgarcrawler.context.CrawlerContext;
import com.example.springfisgarcrawler.utils.DateUtil;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CsvService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @SneakyThrows
    public void writeCsvFile(List<CrawlerContext> results, String name) {
        String filename = name + "_" + LocalDateTime.now().format(DateUtil.toDashFormat()) + ".csv";

        logger.info(String.format("[CSV] Starting to write %s file for %s lines...", name, results.size()));
        Writer writer = new FileWriter(filename);
        StatefulBeanToCsv<CrawlerContext> beanToCsv = new StatefulBeanToCsvBuilder<CrawlerContext>(writer)
                .withOrderedResults(true)
                .build();

        beanToCsv.write(results);
        writer.close();
        logger.info(String.format("[CSV] Serialization for %s completed", name));
    }
}
