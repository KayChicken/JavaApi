package com.example.demo.controller;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;



import com.google.gson.Gson;

public class CrptApi {

    private final long intervalMillis;
    private final int requestLimit;
    private final AtomicInteger requestCount;
    private long lastResetTime;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.intervalMillis = timeUnit.toMillis(1);
        this.requestLimit = requestLimit;
        this.requestCount = new AtomicInteger(0);
        this.lastResetTime = System.currentTimeMillis();
    }

    public synchronized void makeRequest(Document document, String signature) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastResetTime >= intervalMillis) {
            requestCount.set(0);
            lastResetTime = currentTime;

        }

        if (requestCount.get() < requestLimit) {
            requestCount.incrementAndGet();
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpRequest request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(document.convertToJSON()))
            .uri(URI.create("https://api.restful-api.dev/objects"))
            .header("Content-Type", "application/json")
            .build();

        } else {
            
        }
    }

    public static class Product {
        private String certificate_document;
        private String certificate_document_date;
        private String certificate_document_number;
        private String owner_inn;
        private String producer_inn;
        private String production_date;
        private String tnved_code;
        private String uit_code;
        private String uitu_code;

        public Product(String certificate_document, String certificate_document_date,
                String certificate_document_number, String owner_inn,
                String producer_inn, String production_date,
                String tnved_code, String uit_code,
                String uitu_code) {
            this.certificate_document = certificate_document;
            this.certificate_document_date = certificate_document_date;
            this.certificate_document_number = certificate_document_number;
            this.owner_inn = owner_inn;
            this.producer_inn = producer_inn;
            this.production_date = production_date;
            this.tnved_code = tnved_code;
            this.uit_code = uit_code;
            this.uitu_code = uitu_code;
        }
    }

    public static class Document {
        private String description;
        private String doc_id;
        private String doc_status;
        private String doc_type;
        private boolean importRequest;
        private String owner_inn;
        private String participant_inn;
        private String producer_inn;
        private String production_date;
        private String production_type;
        private Product[] products;
        private String reg_date;
        private String reg_number;

        public Document(String description, String doc_id,
                String doc_status, String doc_type,
                boolean importRequest, String owner_inn,
                String participant_inn, String producer_inn,
                String production_date, String production_type,
                Product[] products, String reg_date, String reg_number) {
            this.description = description;
            this.doc_id = doc_id;
            this.doc_status = doc_status;
            this.importRequest = importRequest;
            this.owner_inn = owner_inn;
            this.participant_inn = participant_inn;
            this.producer_inn = producer_inn;
            this.production_type = production_type;
            this.products = products;
            this.reg_date = reg_date;
            this.reg_number = reg_number;

        }

        public String convertToJSON() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

}
