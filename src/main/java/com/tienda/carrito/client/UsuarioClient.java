package com.tienda.carrito.client;

import com.tienda.carrito.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioClient {

    private final RestTemplate restTemplate;

    // "usuario-service" es el spring.application.name que definimos
    // en ese microservicio. Eureka se encarga de traducirlo a host:puerto real.
    private static final String URL_BASE = "http://usuario-service/api/usuarios";

    @Autowired
    public UsuarioClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UsuarioDTO buscarPorId(Long usuarioId) {
        try {
            return restTemplate.getForObject(URL_BASE + "/" + usuarioId, UsuarioDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            return null;
        }
    }
}
