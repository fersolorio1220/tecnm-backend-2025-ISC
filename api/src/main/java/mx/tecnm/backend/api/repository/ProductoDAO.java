package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.Producto;

@Repository
public class ProductoDAO {

        @Autowired
        private JdbcClient conexion;

        // OBTENER TODOS LOS PRODUCTOS
        public List<Producto> consultarProductos() {
                String sql = " SELECT id, nombre, precio, sku, color, marca, descripcion, peso, alto, ancho, profundidad, categorias_id FROM productos WHERE activo = TRUE";
                return conexion.sql(sql)
                                .query((rs, rowNum) -> new Producto(
                                                rs.getInt("id"),
                                                rs.getString("nombre"),
                                                rs.getDouble("precio"),
                                                rs.getString("sku"),
                                                rs.getString("color"),
                                                rs.getString("marca"),
                                                rs.getString("descripcion"),
                                                rs.getDouble("peso"),
                                                rs.getDouble("alto"),
                                                rs.getDouble("ancho"),
                                                rs.getDouble("profundidad"),
                                                rs.getInt("categorias_id")))
                                .list();
        }

        // OBTENER PRODUCTO POR ID
        public Producto obtenerProductoId(int id) {
                String sql = "SELECT id, nombre, precio, sku, color, marca, descripcion, peso, alto, ancho, profundidad, categorias_id FROM productos WHERE id = ? AND activo = TRUE";
                return conexion.sql(sql)
                                .param(id)
                                .query((rs, rowNum) -> new Producto(
                                                rs.getInt("id"),
                                                rs.getString("nombre"),
                                                rs.getDouble("precio"),
                                                rs.getString("sku"),
                                                rs.getString("color"),
                                                rs.getString("marca"),
                                                rs.getString("descripcion"),
                                                rs.getDouble("peso"),
                                                rs.getDouble("alto"),
                                                rs.getDouble("ancho"),
                                                rs.getDouble("profundidad"),
                                                rs.getInt("categorias_id")))
                                .single();
        }
        
        // CREAR NUEVO PRODUCTO
        public Producto crearProducto(String nombre, String sku, String color, String marca,
                              String descripcion, double precio, double peso, double alto,
                              double ancho, double profundidad, int categorias_id) {
                String sql = """
                INSERT INTO productos (nombre, sku, color, marca, descripcion, precio, peso, alto, ancho, profundidad, categorias_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id, nombre, precio, sku, color, marca, descripcion, peso, alto, ancho, profundidad, categorias_id
                """;

                return conexion.sql(sql)
                .params(nombre, sku, color, marca, descripcion, precio, peso, alto, ancho, profundidad, categorias_id)
                .query((rs, rowNum) -> new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getString("sku"),
                        rs.getString("color"),
                        rs.getString("marca"),
                        rs.getString("descripcion"),
                        rs.getDouble("peso"),
                        rs.getDouble("alto"),
                        rs.getDouble("ancho"),
                        rs.getDouble("profundidad"),
                        rs.getInt("categorias_id")))
                .optional().orElse(null);
        }

        // ACTUALIZAR PRODUCTO PARA DATOS EXISTENTES
        public Producto actualizarProducto(int id, String nombre, String sku, String color, String marca,
                              String descripcion, double precio, double peso, double alto,
                              double ancho, double profundidad, int categorias_id) {
                String sql = "UPDATE productos SET nombre = ?, descripcion = ?, marca = ?, color = ?, precio = ?, peso = ?, alto = ?, ancho = ?, profundidad = ?, categorias_id = ?, sku = ? WHERE id = ? AND activo = true";

                int filas = conexion.sql(sql)
                        .params(nombre, descripcion, marca, color, precio, peso, alto, ancho, profundidad, categorias_id, sku, id)
                        .update();
                if (filas == 0) {
                        return null;
                }
                return obtenerProductoId(id);
        }

        // ELIMINAR PRODUCTO (ESTABLECER ACTIVO EN FALSE)
        public boolean eliminarProducto(int id) {
                String sql = "UPDATE productos SET activo = FALSE WHERE id = ?";
                int filas = conexion.sql(sql)
                        .param(id)
                        .update();
                return filas > 0;
        }


        

}
