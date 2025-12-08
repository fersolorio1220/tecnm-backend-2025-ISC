package mx.tecnm.backend.api.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.Categoria;

@Repository
public class CategoriaDAO {

        @Autowired
        private JdbcClient conexion;

        // CONSULTA DE TODAS LAS CATEGORIAS
        public List<Categoria> conusultarCategorias() {
                String sql = "SELECT id, nombre FROM categorias WHERE activo = TRUE";
                return conexion.sql(sql)
                                .query((rs, rowNum) -> new Categoria(
                                                rs.getInt("id"),
                                                rs.getString("nombre")))
                                .list();
        }

        // CONSULTA DE CATEGORIA POR ID
        public Categoria obtenerCategoriaId(int id) {
                String sql = "SELECT id, nombre FROM categorias WHERE id = ? AND activo = TRUE";
                return conexion.sql(sql)
                                .param(id)
                                .query((rs, rowNum) -> new Categoria(
                                                rs.getInt("id"),
                                                rs.getString("nombre")))
                                .optional().orElse(null);
        }

        // CREAR NUEVA CATEGORIA
        public Categoria crearCategoria(String nuevaCategoria) {
                String sql = "INSERT INTO categorias (nombre) VALUES (?) RETURNING id, nombre";
                return conexion.sql(sql)
                                .param(nuevaCategoria)
                                .query((rs, rowNum) -> new Categoria(
                                                rs.getInt("id"),
                                                rs.getString("nombre")))
                                .optional().orElse(null);
        }

        // ACTUALIZAR CATEGORIA PARA DATOS EXISTENTES
        public Categoria actualizarCategoria(int id, String nombre) {
                String sql = "UPDATE categorias SET nombre = ? WHERE id = ? AND activo";
                int filas = conexion.sql(sql)
                                .params(nombre, id)
                                .update();
                if (filas == 0) {
                        return null;
                }
                return obtenerCategoriaId(id);
        }

        // ELIMINAR CATEGORIA (ESTABLECER ACTIVO EN FALSE).
        public boolean eliminarCategoria(int id) {
                String sql = "UPDATE categorias SET activo = FALSE WHERE id = ?";
                int filas = conexion.sql(sql)
                                .param(id)
                                .update();
                return filas > 0;
        }
        
}
