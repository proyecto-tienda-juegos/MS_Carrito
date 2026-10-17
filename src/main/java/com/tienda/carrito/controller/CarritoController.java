package com.tienda.carrito.controller;

import com.tienda.carrito.model.CarritoItem;
import com.tienda.carrito.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    @Autowired
    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    // POST http://localhost:8084/api/carrito
    // Body (JSON): { "usuarioId": 1, "juegoId": 2, "cantidad": 1 }
    // Internamente valida el usuario en usuario-service y el stock en inventario-service
    @PostMapping
    public ResponseEntity<CarritoItem> agregarItem(@Valid @RequestBody CarritoItem item) {
        CarritoItem creado = carritoService.agregarItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // GET http://localhost:8084/api/carrito/usuario/1
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CarritoItem>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.listarPorUsuario(usuarioId));
    }

    // GET http://localhost:8084/api/carrito/usuario/1/total
    @GetMapping("/usuario/{usuarioId}/total")
    public ResponseEntity<Map<String, Object>> calcularTotal(@PathVariable Long usuarioId) {
        double total = carritoService.calcularTotal(usuarioId);
        return ResponseEntity.ok(Map.of("usuarioId", usuarioId, "total", total));
    }

    // DELETE http://localhost:8084/api/carrito/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id) {
        boolean eliminado = carritoService.eliminarItem(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
