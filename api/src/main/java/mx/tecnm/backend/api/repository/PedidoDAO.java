package mx.tecnm.backend.api.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import mx.tecnm.backend.api.models.Pedido;

@Repository
public class PedidoDAO {

     @Autowired
    private JdbcClient conexion;

    public List<Pedido> consultarPedidos(int usuario_id) {

                String sql = "SELECT id, fecha, fecha_hora_pago, importe_envio, importe_iva, importe_productos, metodos_pago_id, numero, total, usuarios_id FROM pedidos WHERE usuarios_id = ? AND activo = TRUE";
                return conexion.sql(sql)
                        .param(usuario_id)
                        .query((rs, rowNum) -> new Pedido(
                        rs.getInt("id"),
                        rs.getTimestamp("fecha"),
                        rs.getTimestamp("fecha_hora_pago"),
                        rs.getDouble("importe_envio"),
                        rs.getDouble("importe_iva"),
                        rs.getDouble("importe_productos"),
                        rs.getInt("metodos_pago_id"),
                        rs.getString("numero"),
                        rs.getDouble("total"),
                        rs.getInt("usuarios_id")
                ))
                .list();
        }

        public Pedido crearPedido(double importe_productos, double importe_envio, int metodo_pago_id, int usuario_id) {
                String sql = "INSERT INTO pedidos (numero, importe_productos, importe_envio, metodos_pago_id, usuarios_id, fecha_hora_pago) VALUES (gen_random_uuid(), ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id, fecha, numero, importe_productos, importe_envio, importe_iva, metodos_pago_id, numero, total, usuarios_id, fecha_hora_pago";

                return conexion.sql(sql)
                        .param(importe_productos)
                        .param(importe_envio)
                        .param(metodo_pago_id)
                        .param(usuario_id)
                        .query((rs, rowNum) -> new Pedido(
                                rs.getInt("id"),
                                rs.getTimestamp("fecha"),
                                rs.getTimestamp("fecha_hora_pago"),
                                rs.getDouble("importe_envio"),
                                rs.getDouble("importe_iva"),
                                rs.getDouble("importe_productos"),
                                rs.getInt("metodos_pago_id"),
                                rs.getString("numero"),
                                rs.getDouble("total"),
                                rs.getInt("usuarios_id")
                        ))
                        .single();
                
        }
}