package com.tienda.carrito.client;

import com.tienda.carrito.dto.InventarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class InventarioClient {

    private final RestTemplate restTemplate;

    private static final String URL_BASE = "http://inventario-service/api/inventario";

    @Autowired
    public InventarioClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public InventarioDTO buscarPorJuegoId(Long juegoId) {
        try {
            return restTemplate.getForObject(URL_BASE + "/juego/" + juegoId, InventarioDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            return null;
        }
    }
}
