package com.tienda.carrito.client;

import com.tienda.carrito.dto.JuegoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class JuegoClient {

    private final RestTemplate restTemplate;

    private static final String URL_BASE = "http://juegos-service/api/juegos";

    @Autowired
    public JuegoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JuegoDTO buscarPorId(Long juegoId) {
        try {
            return restTemplate.getForObject(URL_BASE + "/" + juegoId, JuegoDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            return null;
        }
    }
}
