package com.tienda.carrito.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // @LoadBalanced es la magia que permite escribir URLs como
    // "http://usuario-service/api/usuarios/1" en vez de tener que
    // saber el puerto exacto (8081, 8082, etc). Eureka resuelve el nombre.
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
