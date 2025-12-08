package mx.tecnm.backend.api.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.MetodoPago;

@Repository
public class MetodoPagoDAO {

    @Autowired
    private JdbcClient conexion;

    //OBTENER TODOS LOS METODOS DE PAGO
    public List<MetodoPago> consultarMetodosPago() {
        String sql = "SELECT id, nombre, comision FROM metodos_pago WHERE activo = TRUE";
        return conexion.sql(sql)
            .query((rs, rowNum) -> new MetodoPago(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getBigDecimal("comision")
            ))
            .list();
    }

    //OBTENER METODO DE PAGO POR ID
    public MetodoPago obtenerMetodoPagoId(int id) {
        String sql = "SELECT id, nombre, comision FROM metodos_pago WHERE id = ? AND activo = TRUE";
        return conexion.sql(sql)
            .param(id)
            .query((rs, rowNum) -> new MetodoPago(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getBigDecimal("comision")
            ))
            .single();
    }

    // CREAR NUEVO MÉTODO DE PAGO
    public MetodoPago crearMetodoPago(String nombre, double comision) {
        String sql = "INSERT INTO metodos_pago (nombre, comision) VALUES (?, ?) RETURNING id, nombre, comision";
        
        return conexion.sql(sql)
                .params(nombre, comision)
                .query((rs, rowNum) -> new MetodoPago(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("comision")))
                .optional()
                .orElse(null);
    }

    // ACTUALIZAR MÉTODO DE PAGO (SOLO SI ESTA ACTIVO)
    public MetodoPago actualizarMetodoPago(int id, String nombre, double comision) {
        String sql = "UPDATE metodos_pago SET nombre = ?, comision = ? WHERE id = ? AND activo";
        
        int filas = conexion.sql(sql)
                .params(nombre, comision, id)
                .update();
        if (filas == 0) {
            return null;
        }

        return obtenerMetodoPagoId(id);
    }

    // ELIMINAR MÉTODO DE PAGO (BAJA LÓGICA)
    public boolean eliminarMetodoPago(int id) {
        String sql = "UPDATE metodos_pago SET activo = FALSE WHERE id = ?";
        
        int filas = conexion.sql(sql)
                .param(id)
                .update();

        return filas > 0;
    }


}