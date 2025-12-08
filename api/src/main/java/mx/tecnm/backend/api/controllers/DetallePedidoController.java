package mx.tecnm.backend.api.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import mx.tecnm.backend.api.models.DetallePedido;
import mx.tecnm.backend.api.repository.DetallePedidoDAO;

@RestController
@RequestMapping("/usuarios/{usuario_id}/pedidos/{pedido_id}/detalles")
public class DetallePedidoController {

    @Autowired
    DetallePedidoDAO repo;

    @GetMapping()
    public ResponseEntity<List<DetallePedido>> obtenerDetallesPedido(@PathVariable int usuario_id, @PathVariable int pedido_id) {
        List<DetallePedido> resultado = repo.obtenerDetallesPorPedidoUsuario(usuario_id, pedido_id);
        return ResponseEntity.ok(resultado);
    }
}