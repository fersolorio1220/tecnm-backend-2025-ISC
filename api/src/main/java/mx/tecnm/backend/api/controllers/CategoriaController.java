package mx.tecnm.backend.api.controllers;

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
import mx.tecnm.backend.api.models.Categoria;
import mx.tecnm.backend.api.repository.CategoriaDAO;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    CategoriaDAO repo;

    // OBTENER TODAS LAS CATEGORIAS
    @GetMapping()
    public ResponseEntity<List<Categoria>> obtenerCategorias() {
        List<Categoria> resultado = repo.conusultarCategorias();
        return ResponseEntity.ok(resultado);
    }

    // OBTENER CATEGORIA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaId(@PathVariable int id) {
        Categoria categoria = repo.obtenerCategoriaId(id);

        if (categoria == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(categoria);
        }
    }

    // CREAR NUEVA CATEGORIA
    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@RequestParam String nuevaCategoria) {
        Categoria categoriaCreada = repo.crearCategoria(nuevaCategoria);
        return ResponseEntity.ok(categoriaCreada);
    }

    // ACTUALIZAR CATEGORIA PARA DATOS EXISTENTES
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable int id, @RequestParam String nombre) {
        Categoria categoriaActualizada = repo.actualizarCategoria(id, nombre);

        if (categoriaActualizada == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(categoriaActualizada);
        }
    }

    // ELIMINAR CATEGORIA (ESTABLECER ACTIVO EN FALSE)
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable int id) {
        boolean eliminado = repo.eliminarCategoria(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}