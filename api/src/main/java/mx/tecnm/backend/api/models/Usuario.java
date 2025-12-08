package mx.tecnm.backend.api.models;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record Usuario
    (int id, String nombre,
    String email,
    String telefono,
    String sexo,
    LocalDate fecha_nacimiento,
    String contrasena,
    OffsetDateTime fecha_registro) { 
}