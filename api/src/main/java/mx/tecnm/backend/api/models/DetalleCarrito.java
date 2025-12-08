package mx.tecnm.backend.api.models;

public record DetalleCarrito(
    int id,
    int cantidad,
    double precio,
    int producto_id,
    int usuario_id
) {}
