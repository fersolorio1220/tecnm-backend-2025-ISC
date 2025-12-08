package mx.tecnm.backend.api.models;
import java.sql.Timestamp;

public record Pedido(
    int id,
    Timestamp fecha,
    Timestamp fecha_hora_pago,
    double importe_envio,
    double importe_iva, 
    double importe_productos,
    int metodos_pago_id,
    String numero,
    double total,
    int usuarios_id
) 
{}
