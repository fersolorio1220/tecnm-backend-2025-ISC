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

import mx.tecnm.backend.api.models.MetodoPago;
import mx.tecnm.backend.api.repository.MetodoPagoDAO;

@RestController
@RequestMapping("/metodospago")
public class MetodoPagoController {

    @Autowired
    MetodoPagoDAO repo;

    //OBTENER TODOS LOS METODOS DE PAGO
    @GetMapping()
    public ResponseEntity<List<MetodoPago>> obtenerMetodosPago() {
        List<MetodoPago> resultado = repo.consultarMetodosPago();
        return ResponseEntity.ok(resultado);
    }

    //OBTENER METODO DE PAGO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<MetodoPago> obtenerMetodoPagoId(@PathVariable int id){
        MetodoPago metodoPago = repo.obtenerMetodoPagoId(id);

        if(metodoPago == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(metodoPago);
        }
    }

    // CREAR NUEVO MÉTODO DE PAGO
    @PostMapping
    public ResponseEntity<MetodoPago> crearMetodoPago(@RequestParam String nombre,@RequestParam double comision) {
        MetodoPago metodoCreado = repo.crearMetodoPago(nombre, comision);
        return ResponseEntity.ok(metodoCreado);
    }

    // ACTUALIZAR MÉTODO DE PAGO PARA DATOS EXISTENTES
    @PutMapping("/{id}")
    public ResponseEntity<MetodoPago> actualizarMetodoPago(@PathVariable int id,@RequestParam String nombre,@RequestParam double comision) {
        MetodoPago metodoActualizado = repo.actualizarMetodoPago(id, nombre, comision);
        if (metodoActualizado == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(metodoActualizado);
        }
    }

    // ELIMINAR MÉTODO DE PAGO (ESTABLECER ACTIVO = FALSE)
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarMetodoPago(@PathVariable int id) {
        boolean eliminado = repo.eliminarMetodoPago(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}