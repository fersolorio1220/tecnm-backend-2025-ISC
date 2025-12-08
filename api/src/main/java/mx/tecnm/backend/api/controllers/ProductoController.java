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

import mx.tecnm.backend.api.models.Producto;
import mx.tecnm.backend.api.repository.ProductoDAO;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    ProductoDAO repo;

    //OBTENER TODOS LOS PRODUCTOS
    @GetMapping()
    public ResponseEntity<List<Producto>> obtenerProductos() {
        List<Producto> resultado = repo.consultarProductos();
        return ResponseEntity.ok(resultado);
    }

    //OBTENER PRODUCTO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoId(@PathVariable int id){
        Producto producto = repo.obtenerProductoId(id);

        if(producto == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(producto);
        }
    }

    /*// CREAR NUEVO PRODUCTO
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestParam String nombre,@RequestParam String descripcion,@RequestParam double precio,@RequestParam int existencias,@RequestParam int categoria_id){
        Producto productoCreado = repo.crearProducto(nombre, descripcion, precio, existencias, categoria_id);
        return ResponseEntity.ok(productoCreado);
    }*/
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestParam String nombre,@RequestParam String sku,@RequestParam String color,@RequestParam String marca,@RequestParam String descripcion,@RequestParam double precio,@RequestParam double peso,@RequestParam double alto,@RequestParam double ancho,@RequestParam double profundidad,@RequestParam int categorias_id) {
        Producto productoCreado = repo.crearProducto(nombre, sku, color, marca, descripcion, precio, peso, alto, ancho, profundidad, categorias_id
        );

        return ResponseEntity.ok(productoCreado);
    }



    // ACTUALIZAR PRODUCTO PARA DATOS EXISTENTES
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable int id,@RequestParam String nombre,@RequestParam String sku,@RequestParam String color,@RequestParam String marca,@RequestParam String descripcion,@RequestParam double precio,@RequestParam double peso,@RequestParam double alto,@RequestParam double ancho,@RequestParam double profundidad,@RequestParam int categorias_id){
        Producto productoActualizado = repo.actualizarProducto(id,nombre, sku, color, marca, descripcion, precio, peso, alto, ancho, profundidad, categorias_id);
;

        if(productoActualizado == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(productoActualizado);
        }
    }

    // ELIMINAR PRODUCTO (ESTABLECER ACTIVO EN FALSE)
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable int id){
        boolean eliminado = repo.eliminarProducto(id);

        if(eliminado){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

}