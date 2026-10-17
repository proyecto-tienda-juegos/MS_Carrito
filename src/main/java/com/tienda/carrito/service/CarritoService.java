package com.tienda.carrito.service;

import com.tienda.carrito.client.InventarioClient;
import com.tienda.carrito.client.JuegoClient;
import com.tienda.carrito.client.UsuarioClient;
import com.tienda.carrito.dto.InventarioDTO;
import com.tienda.carrito.dto.JuegoDTO;
import com.tienda.carrito.dto.UsuarioDTO;
import com.tienda.carrito.model.CarritoItem;
import com.tienda.carrito.repository.CarritoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    private final CarritoItemRepository carritoItemRepository;
    private final UsuarioClient usuarioClient;
    private final InventarioClient inventarioClient;
    private final JuegoClient juegoClient;

    @Autowired
    public CarritoService(CarritoItemRepository carritoItemRepository,
                           UsuarioClient usuarioClient,
                           InventarioClient inventarioClient,
                           JuegoClient juegoClient) {
        this.carritoItemRepository = carritoItemRepository;
        this.usuarioClient = usuarioClient;
        this.inventarioClient = inventarioClient;
        this.juegoClient = juegoClient;
    }

    public CarritoItem agregarItem(CarritoItem item) {
        // 1. Validar que el usuario exista (llamada a usuario-service)
        UsuarioDTO usuario = usuarioClient.buscarPorId(item.getUsuarioId());
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe el usuario con id " + item.getUsuarioId());
        }

        // 2. Validar que haya stock suficiente (llamada a inventario-service)
        InventarioDTO inventario = inventarioClient.buscarPorJuegoId(item.getJuegoId());
        if (inventario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No hay registro de inventario para el juego " + item.getJuegoId());
        }
        if (inventario.getCantidadDisponible() < item.getCantidad()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Stock insuficiente: quedan " + inventario.getCantidadDisponible()
                            + " unidades del juego " + item.getJuegoId());
        }

        // 3. Si pasa las dos validaciones, se guarda el item en el carrito
        return carritoItemRepository.save(item);
    }

    public List<CarritoItem> listarPorUsuario(Long usuarioId) {
        return carritoItemRepository.findByUsuarioId(usuarioId);
    }

    public boolean eliminarItem(Long id) {
        if (carritoItemRepository.existsById(id)) {
            carritoItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Calcula el total sumando cantidad * precio de cada item.
    // El precio se consulta en vivo a juegos-service (no se guarda duplicado en Carrito).
    public double calcularTotal(Long usuarioId) {
        List<CarritoItem> items = carritoItemRepository.findByUsuarioId(usuarioId);
        double total = 0.0;
        for (CarritoItem item : items) {
            JuegoDTO juego = juegoClient.buscarPorId(item.getJuegoId());
            if (juego != null && juego.getPrecio() != null) {
                total += juego.getPrecio() * item.getCantidad();
            }
        }
        return total;
    }

    public Optional<CarritoItem> buscarPorId(Long id) {
        return carritoItemRepository.findById(id);
    }
}
