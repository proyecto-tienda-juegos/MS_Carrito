package com.tienda.carrito.repository;

import com.tienda.carrito.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    List<CarritoItem> findByUsuarioId(Long usuarioId);
}
