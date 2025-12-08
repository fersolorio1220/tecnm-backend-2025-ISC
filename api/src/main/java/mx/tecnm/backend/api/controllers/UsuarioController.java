package mx.tecnm.backend.api.controllers;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mx.tecnm.backend.api.models.Usuario;
import mx.tecnm.backend.api.repository.UsuarioDAO;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
   
    @Autowired
    UsuarioDAO repo;

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        List<Usuario> resultado = repo.consultarUsuarios();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable int id) {
        Usuario usuario = repo.obtenerUsuarioId(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/crear")
    public ResponseEntity<Usuario> crearUsuario(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam String sexo,
            @RequestParam Date fecha_nacimiento,
            @RequestParam String contrasena) {
        Usuario usuario = repo.crearUsuario(
                nombre,
                email,
                telefono,
                sexo,
                fecha_nacimiento.toLocalDate(),
                contrasena);

        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable int id,
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam String sexo,
            @RequestParam Date fecha_nacimiento,
            @RequestParam String contrasena) {

        Usuario existente = repo.obtenerUsuarioId(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        Usuario actualizado = repo.actualizarUsuario(
                new Usuario(id, nombre, email, telefono, sexo, fecha_nacimiento.toLocalDate(),contrasena, existente.fecha_registro()));

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable int id) {

        boolean eliminado = repo.eliminarUsuario(id);

        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
    
}