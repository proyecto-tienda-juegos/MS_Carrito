package com.tienda.carrito.dto;

// Solo los campos que Carrito necesita leer de Usuario.
// No es la misma clase que la entidad Usuario (esa vive en usuario-service).
public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String email;

    public UsuarioDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
