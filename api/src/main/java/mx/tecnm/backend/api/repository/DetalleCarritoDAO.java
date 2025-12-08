package mx.tecnm.backend.api.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import mx.tecnm.backend.api.models.DetalleCarrito;
import mx.tecnm.backend.api.models.Pedido;

@Repository
public class DetalleCarritoDAO {
    @Autowired
    private JdbcClient conexion;
    
    @Autowired
    private ProductoDAO producto_repo;

    @Autowired
    private PedidoDAO pedido_repo;

    @Autowired
    private DetallePedidoDAO detalle_pedido_repo;

    public List<DetalleCarrito> obtenerCarrito(int usuario_id) {
        String sql = "SELECT id, cantidad, precio, productos_id FROM detalles_carrito WHERE usuarios_id = ? AND activo = TRUE";

        return conexion.sql(sql)
                .param(usuario_id)
                .query((rs, rowNum) -> new DetalleCarrito(
                        rs.getInt("id"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        rs.getInt("productos_id"),
                        usuario_id))
                .list();
    }

    public DetalleCarrito buscarProductoEnCarrito(int usuario_id, int producto_id) {
        String sql = "SELECT id, cantidad, precio FROM detalles_carrito WHERE usuarios_id = ? AND productos_id = ? AND activo = TRUE";

        return conexion.sql(sql)
                .param(usuario_id)
                .param(producto_id)
                .query((rs, rowNum) -> new DetalleCarrito(
                        rs.getInt("id"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        producto_id,
                        usuario_id))
                .optional()
                .orElse(null);
    }

    public DetalleCarrito agregarAlCarrito(int usuario_id, int producto_id, int cantidad) {
        String sql = "INSERT INTO detalles_carrito (cantidad, precio, productos_id, usuarios_id) VALUES (?, ?, ?, ?) RETURNING id";

        double precioUnitario = producto_repo.obtenerProductoId(producto_id).precio();
        double precioTotal = precioUnitario * cantidad;

        return conexion.sql(sql)
                .param(cantidad)
                .param(precioTotal)
                .param(producto_id)
                .param(usuario_id)
                .query((rs, rowNum) -> new DetalleCarrito(
                        rs.getInt("id"),
                        cantidad,
                        precioTotal,
                        producto_id,
                        usuario_id))
                .single();
    }

    public DetalleCarrito actualizarCantidadEnCarrito(int detalle_carrito_id, int nueva_cantidad) {
        String sql = "UPDATE detalles_carrito SET cantidad = ?, precio = ? WHERE id = ? RETURNING usuarios_id, productos_id";

        // Obtener el producto_id y usuario_id actuales
        DetalleCarrito detalleActual = conexion.sql("SELECT productos_id, usuarios_id FROM detalles_carrito WHERE id = ?")
                .param(detalle_carrito_id)
                .query((rs, rowNum) -> new DetalleCarrito(
                        detalle_carrito_id,
                        0, // cantidad temporal
                        0.0, // precio temporal
                        rs.getInt("productos_id"),
                        rs.getInt("usuarios_id")))
                .single();

        double precioUnitario = producto_repo.obtenerProductoId(detalleActual.producto_id()).precio();
        double nuevoPrecioTotal = precioUnitario * nueva_cantidad;

        return conexion.sql(sql)
                .param(nueva_cantidad)
                .param(nuevoPrecioTotal)
                .param(detalle_carrito_id)
                .query((rs, rowNum) -> new DetalleCarrito(
                        detalle_carrito_id,
                        nueva_cantidad,
                        nuevoPrecioTotal,
                        detalleActual.producto_id(),
                        detalleActual.usuario_id()))
                .single();
    }

    public boolean borrarDelCarrito(int usuario_id, int producto_id, int cantidad) {
        DetalleCarrito detalle = buscarProductoEnCarrito(usuario_id, producto_id);
        if (detalle == null) {
            return false; // No existe el producto en el carrito
        }

        int nuevaCantidad = detalle.cantidad() - cantidad;
        if (nuevaCantidad > 0) {
            // Actualizar la cantidad y el precio
            actualizarCantidadEnCarrito(detalle.id(), nuevaCantidad);
        } else {
            // Eliminar el detalle del carrito
            String sql = "DELETE FROM detalles_carrito WHERE id = ?";
            conexion.sql(sql)
                    .param(detalle.id())
                    .update();
        }
        return true;
    }

    public boolean checkoutCarrito(int usuario_id, int metodo_pago_id) {

        List<DetalleCarrito> lista = obtenerCarrito(usuario_id);

        if (lista.isEmpty())
        {
            return false; // No se puede hacer checkout de un carrito vacío
        }

        Pedido pedidoNuevo = pedido_repo.crearPedido(
            lista.stream().mapToDouble(DetalleCarrito::precio).sum(),
            100.0,
            metodo_pago_id,
            usuario_id
        );

        for (DetalleCarrito detalle : lista)
        {
            detalle_pedido_repo.crearDetallePedido(
                detalle.cantidad(),
                detalle.precio(),
                detalle.producto_id(),
                pedidoNuevo.id()
            );
        }

        String sql = "DELETE FROM detalles_carrito WHERE usuarios_id = ?";
        conexion.sql(sql)
                .param(usuario_id)
                .update();
        return true;
    }
}