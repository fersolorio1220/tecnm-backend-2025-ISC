package mx.tecnm.backend.api.models;

import java.math.BigDecimal;

public record MetodoPago(
    int id,
    String nombre,
    BigDecimal comision
) 
{}