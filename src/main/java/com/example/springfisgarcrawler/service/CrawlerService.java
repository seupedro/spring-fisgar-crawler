package com.example.springfisgarcrawler.service;

import com.example.springfisgarcrawler.context.CrawlerContext;
import com.example.springfisgarcrawler.executor.HttpExecutor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class CrawlerService {

    private static final String[] REQUEST_HEADERS = new String[]{
            "Accept", "*/*",
            "Accept-Language","Language: pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7",
            "Content-Type","text/x-gwt-rpc; charset=UTF-8",
            "Origin", "http://www.sigt.osasco.sp.gov.br",
            "Pragma", "no-cache",
            "Referer", "http://www.sigt.osasco.sp.gov.br/iTRIB2/IptEmitirDAMDoIPTUExibir_Internet.do",
            "User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36",
            "X-GWT-Module-Base", "http://www.sigt.osasco.sp.gov.br/iTRIB2/gwt-www/br.trib.ipt.gwt.emitirDAMDoIPTU.EmitirDAMDoIPTU",
            "X-GWT-Permutation", "303E6A562E585286A9F62B7E98A69A36",
    };

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CsvService csvService;
    private final HttpExecutor httpExecutor;

    public CrawlerService(CsvService csvService, HttpExecutor httpExecutor) {
        this.csvService = csvService;
        this.httpExecutor = httpExecutor;
    }

    public void launch(String[] args) {
        int start = Integer.parseInt(args[0]);
        int end = Integer.parseInt(args[1]);

        IntStream.range(start, end).forEach(this::toExecutionPlan);
        csvService.terminate();
        logger.info("[Final Report] Execution Completed: ✅");
    }

    private void toExecutionPlan(Integer i) {
        Optional.of(i)
                .map(this::toContext)
                .map(this::toBodyString)
                .map(this::toPostRequest)
                .map(httpExecutor::toResponse)
                .filter(this::relevantResponse)
                .ifPresent(csvService::writeCsvFile);
    }

    private CrawlerContext toContext(int index) {
        return CrawlerContext.builder().iptuId(index).build();
    }

    private CrawlerContext toBodyString(CrawlerContext context) {
        StringBuilder builder = new StringBuilder();
        builder.append("7|0|8|http://www.sigt.osasco.sp.gov.br/iTRIB2/gwt-www/br.trib.ipt.gwt.emitirDAMDoIPTU.EmitirDAMDoIPTU/|ED02C754C3C950A0F9AEFC3436522F1D|br.trib.lot.gwt.selecionarLoteSublote.client.ServicoRemoto|pesquisarSublotesPorNumMatricula|java.lang.String/2004016611|br.trib.app.gwt.client.listaDeRegistros.PaginacaoDTO/1551720962|");
        builder.append(context.getIptuId());
        builder.append("|java.util.ArrayList/4159755760|1|2|3|4|2|5|6|7|6|1|0|0|8|0|");

        context.setRequestBody(builder.toString());
        return context;
    }

    private CrawlerContext toPostRequest(CrawlerContext context) {
        URI uri = URI.create("http://www.sigt.osasco.sp.gov.br/iTRIB2/lot/SelecionarLoteSublote");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(context.getRequestBody()))
                .headers(REQUEST_HEADERS)
                .timeout(Duration.ofSeconds(1L))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        context.setHttpRequest(request);
        context.setUri(uri);
        return context;
    }

    private boolean relevantResponse(CrawlerContext context) {
        String requestBody = context.getRequestBody();
        return Objects.nonNull(requestBody) && !Strings.isBlank(requestBody);
    }
}