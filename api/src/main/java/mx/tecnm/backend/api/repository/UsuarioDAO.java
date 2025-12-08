package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.Usuario;

@Repository
public class UsuarioDAO {

    @Autowired
    private JdbcClient conexion;

    // OBTENER TODOS LOS USUARIOS
    public List<Usuario> consultarUsuarios() {
        String sql = "SELECT id, nombre, email, telefono, sexo, fecha_nacimiento, contrasena, fecha_registro FROM usuarios WHERE activo = TRUE";
        return conexion.sql(sql)
                .query((rs, rowNum) -> new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("sexo"),
                        rs.getObject("fecha_nacimiento", java.time.LocalDate.class),
                        rs.getString("contrasena"),
                        rs.getObject("fecha_registro", java.time.OffsetDateTime.class)))
                .list();
    }

    // OBTENER USUARIO POR ID
    public Usuario obtenerUsuarioId(int id) {
        String sql = "SELECT id, nombre, email, telefono, sexo, fecha_nacimiento, contrasena, fecha_registro FROM usuarios WHERE id = ? AND activo = TRUE";
        return conexion.sql(sql)
                .param(id)
                .query((rs, rowNum) -> new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("sexo"),
                        rs.getObject("fecha_nacimiento", java.time.LocalDate.class),
                        rs.getString("contrasena"),
                        rs.getObject("fecha_registro", java.time.OffsetDateTime.class)))
                .single();
    }

    // CREAR USUARIO
    public Usuario crearUsuario(String nombre, String email, String telefono, String sexo, java.time.LocalDate fecha_nacimiento, String contrasena) {
        String sql = "INSERT INTO usuarios (nombre, email, telefono, sexo, fecha_nacimiento, contrasena) VALUES (?, ?, ?, CAST(? AS sexo_enum), ?, ?) RETURNING id, nombre, email, telefono, sexo, fecha_nacimiento, contrasena, fecha_registro";
        return conexion.sql(sql)
                .params(nombre, email, telefono, sexo, fecha_nacimiento, contrasena)
                .query((rs, rowNum) -> new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("sexo"),
                        rs.getObject("fecha_nacimiento", java.time.LocalDate.class),
                        rs.getString("contrasena"),
                        rs.getObject("fecha_registro", java.time.OffsetDateTime.class)))
                .single();
    }

    // ACTUALIZAR USUARIO
    public Usuario actualizarUsuario(Usuario u) {
        String sql = " UPDATE usuarios SET nombre = ?, email = ?, telefono = ?, sexo = CAST(? AS sexo_enum), fecha_nacimiento = ?, contrasena = ? WHERE activo=true AND id = ?";
        int filas = conexion.sql(sql)
                .params(
                        u.nombre(),
                        u.email(),
                        u.telefono(),
                        u.sexo(),
                        u.fecha_nacimiento(),
                        u.contrasena(),
                        u.id())
                .update();

        if (filas == 0)
            return null;
        return obtenerUsuarioId(u.id());
    }

        // ELIMINAR USUARIO )
    public boolean eliminarUsuario(int id) {
                String sql = "UPDATE usuarios SET activo = false WHERE id = ?";

                int filas = conexion.sql(sql)
                        .param(id)
                        .update();
                return filas > 0;
        }

}