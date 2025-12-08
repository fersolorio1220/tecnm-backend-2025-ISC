package mx.tecnm.backend.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.tecnm.backend.api.models.DetalleCarrito;
import mx.tecnm.backend.api.repository.DetalleCarritoDAO;

@RestController
@RequestMapping("/usuarios/{usuario_id}/carrito")
public class DetalleCarritoController {

    @Autowired
    DetalleCarritoDAO repo;

    @GetMapping
    ResponseEntity<List<DetalleCarrito>> obtenerCarrito(@PathVariable int usuario_id)
    {
        List<DetalleCarrito> resultado = repo.obtenerCarrito(usuario_id);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    ResponseEntity<DetalleCarrito> agregarAlCarrito(@PathVariable int usuario_id, @RequestParam int producto_id, @RequestParam int cantidad)
    {
        DetalleCarrito busqueda = repo.buscarProductoEnCarrito(usuario_id, producto_id);
        DetalleCarrito resultado;
        if (busqueda == null)
        {
            resultado = repo.agregarAlCarrito(usuario_id, producto_id, cantidad);
        }
        else
        {
            resultado = repo.actualizarCantidadEnCarrito(busqueda.id(), busqueda.cantidad() + cantidad);
        }

        if (resultado == null) {
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping()
    ResponseEntity<Void> borrarDelCarrito(@PathVariable int usuario_id, @RequestParam int producto_id, @RequestParam int cantidad)
    {
        boolean exito = repo.borrarDelCarrito(usuario_id, producto_id, cantidad);
        if (exito) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/checkout")
    ResponseEntity<Void> checkoutCarrito(@PathVariable int usuario_id, @RequestParam int metodo_pago_id)
    {
        boolean exito = repo.checkoutCarrito(usuario_id, metodo_pago_id);
        if (exito) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }
}
