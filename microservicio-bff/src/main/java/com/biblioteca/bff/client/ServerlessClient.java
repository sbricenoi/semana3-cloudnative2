package com.biblioteca.bff.client;

import com.biblioteca.bff.exception.ServerlessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class ServerlessClient {
    private static final Logger logger = LoggerFactory.getLogger(ServerlessClient.class);
    private final RestTemplate restTemplate;

    public ServerlessClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        try {
            logger.info("GET request to: {}", url);
            ResponseEntity<T> response = restTemplate.getForEntity(url, responseType);
            logger.info("GET response status: {}", response.getStatusCode());
            return response;
        } catch (HttpClientErrorException e) {
            logger.error("Client error on GET {}: {}", url, e.getMessage());
            throw new ServerlessException("Error en función serverless: " + e.getStatusCode(), e);
        } catch (ResourceAccessException e) {
            logger.error("Connection error on GET {}: {}", url, e.getMessage());
            throw new ServerlessException("No se pudo conectar con la función serverless", e);
        } catch (Exception e) {
            logger.error("Unexpected error on GET {}: {}", url, e.getMessage());
            throw new ServerlessException("Error inesperado al llamar función serverless", e);
        }
    }

    public <T, R> ResponseEntity<R> post(String url, T body, Class<R> responseType) {
        try {
            logger.info("POST request to: {}", url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<T> request = new HttpEntity<>(body, headers);
            
            ResponseEntity<R> response = restTemplate.postForEntity(url, request, responseType);
            logger.info("POST response status: {}", response.getStatusCode());
            return response;
        } catch (HttpClientErrorException e) {
            logger.error("Client error on POST {}: {}", url, e.getMessage());
            throw new ServerlessException("Error en función serverless: " + e.getStatusCode(), e);
        } catch (ResourceAccessException e) {
            logger.error("Connection error on POST {}: {}", url, e.getMessage());
            throw new ServerlessException("No se pudo conectar con la función serverless", e);
        } catch (Exception e) {
            logger.error("Unexpected error on POST {}: {}", url, e.getMessage());
            throw new ServerlessException("Error inesperado al llamar función serverless", e);
        }
    }

    public <T, R> ResponseEntity<R> put(String url, T body, Class<R> responseType) {
        try {
            logger.info("PUT request to: {}", url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<T> request = new HttpEntity<>(body, headers);
            
            ResponseEntity<R> response = restTemplate.exchange(url, HttpMethod.PUT, request, responseType);
            logger.info("PUT response status: {}", response.getStatusCode());
            return response;
        } catch (HttpClientErrorException e) {
            logger.error("Client error on PUT {}: {}", url, e.getMessage());
            throw new ServerlessException("Error en función serverless: " + e.getStatusCode(), e);
        } catch (ResourceAccessException e) {
            logger.error("Connection error on PUT {}: {}", url, e.getMessage());
            throw new ServerlessException("No se pudo conectar con la función serverless", e);
        } catch (Exception e) {
            logger.error("Unexpected error on PUT {}: {}", url, e.getMessage());
            throw new ServerlessException("Error inesperado al llamar función serverless", e);
        }
    }

    public <T> ResponseEntity<T> delete(String url, Class<T> responseType) {
        try {
            logger.info("DELETE request to: {}", url);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.DELETE, null, responseType);
            logger.info("DELETE response status: {}", response.getStatusCode());
            return response;
        } catch (HttpClientErrorException e) {
            logger.error("Client error on DELETE {}: {}", url, e.getMessage());
            throw new ServerlessException("Error en función serverless: " + e.getStatusCode(), e);
        } catch (ResourceAccessException e) {
            logger.error("Connection error on DELETE {}: {}", url, e.getMessage());
            throw new ServerlessException("No se pudo conectar con la función serverless", e);
        } catch (Exception e) {
            logger.error("Unexpected error on DELETE {}: {}", url, e.getMessage());
            throw new ServerlessException("Error inesperado al llamar función serverless", e);
        }
    }
}
