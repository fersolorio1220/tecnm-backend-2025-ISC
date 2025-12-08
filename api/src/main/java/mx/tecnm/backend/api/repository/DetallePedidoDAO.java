package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.DetallePedido;

@Repository
public class DetallePedidoDAO {

    @Autowired
    private JdbcClient conexion;

    public List<DetallePedido> obtenerDetallesPedido() {
        String sql = "SELECT id, cantidad, precio, productos_id, pedidos_id FROM detalles_pedido WHERE activo = TRUE";

        return conexion.sql(sql)
                .query((rs, rowNum) -> new DetallePedido(
                        rs.getInt("id"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        rs.getInt("productos_id"),
                        rs.getInt("pedidos_id")))
                .list();

    }

    public DetallePedido crearDetallePedido(int cantidad, double precio, int producto_id, int pedido_id) {
        String sql = "INSERT INTO detalles_pedido (cantidad, precio, productos_id, pedidos_id) VALUES (?, ?, ?, ?) RETURNING id, cantidad, precio, productos_id, pedidos_id WHERE activo = TRUE";

        return conexion.sql(sql)
                .params(cantidad, precio, producto_id, pedido_id)
                .query((rs, rowNum) -> new DetallePedido(
                        rs.getInt("id"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        rs.getInt("productos_id"),
                        rs.getInt("pedidos_id")))
                .single();
    }

    public List<DetallePedido> obtenerDetallesPorPedidoUsuario(int usuario_id, int pedido_id) {
        String sql = "SELECT dp.id, dp.cantidad, dp.precio, dp.productos_id, dp.pedidos_id " +
                     "FROM detalles_pedido dp " +
                     "JOIN pedidos p ON dp.pedidos_id = p.id " +
                     "WHERE p.usuarios_id = ? AND p.id = ?";

        return conexion.sql(sql)
                .param(usuario_id)
                .param(pedido_id)
                .query((rs, rowNum) -> new DetallePedido(
                        rs.getInt("id"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        rs.getInt("productos_id"),
                        rs.getInt("pedidos_id")))
                .list();
    }

}
