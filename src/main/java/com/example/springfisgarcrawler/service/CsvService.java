package com.example.springfisgarcrawler.service;

import com.example.springfisgarcrawler.context.CrawlerContext;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Writer;

@Component
public class CsvService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final StatefulBeanToCsv<CrawlerContext> beanToCsv;
    private final Writer writer;

    public CsvService(StatefulBeanToCsv<CrawlerContext> beanToCsv, Writer writer) {
        this.beanToCsv = beanToCsv;
        this.writer = writer;
    }

    public void writeCsvFile(CrawlerContext context) {
        try {
            logger.info(String.format("[CSV] Appending CSV lines %s", context.getResponseBody()));
            beanToCsv.write(context);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public void terminate() {
        writer.close();
        logger.info("[CSV] Serialization for %s completed");
    }
}
