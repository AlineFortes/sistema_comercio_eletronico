package com.ecommerce.dao;

import com.ecommerce.enums.StatusPedido;
import com.ecommerce.model.ItemPedido;
import com.ecommerce.model.Pedido;
import com.ecommerce.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para Pedido.
 */
public class PedidoDAO {

    private Connection getConexao() throws SQLException {
        return ConexaoBD.getConexao();
    }

    public void inserir(Pedido pedido) throws SQLException {
        String sqlPedido = "INSERT INTO pedidos (usuario_id, valor_total, status, endereco_entrega) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = getConexao().prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pedido.getUsuario().getId());
            stmt.setDouble(2, pedido.getValorTotal());
            stmt.setString(3, pedido.getStatus().name());
            stmt.setString(4, pedido.getEnderecoEntrega());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) pedido.setId(rs.getInt(1));
            inserirItens(pedido);
            getConexao().commit();
        }
    }

    private void inserirItens(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO itens_pedido (pedido_id, produto_id, nome_produto, preco_unitario, quantidade, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            for (ItemPedido item : pedido.getItens()) {
                stmt.setInt(1, pedido.getId());
                stmt.setInt(2, item.getProdutoId());
                stmt.setString(3, item.getNomeProduto());
                stmt.setDouble(4, item.getPrecoUnitario());
                stmt.setInt(5, item.getQuantidade());
                stmt.setDouble(6, item.getSubtotal());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public void atualizarStatus(int id, StatusPedido status) throws SQLException {
        String sql = "UPDATE pedidos SET status=? WHERE id=?";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();
            getConexao().commit();
        }
    }

    public Pedido buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM pedidos WHERE id=?";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapearPedido(rs);
        }
        return null;
    }

    public List<Pedido> listarPorUsuario(int usuarioId) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE usuario_id=? ORDER BY data_pedido DESC";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) pedidos.add(mapearPedido(rs));
        }
        return pedidos;
    }

    public List<Pedido> listarTodos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos ORDER BY data_pedido DESC";
        try (Statement stmt = getConexao().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) pedidos.add(mapearPedido(rs));
        }
        return pedidos;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getInt("id"));
        p.setValorTotal(rs.getDouble("valor_total"));
        p.setDataPedido(rs.getString("data_pedido"));
        p.setStatus(StatusPedido.valueOf(rs.getString("status")));
        p.setEnderecoEntrega(rs.getString("endereco_entrega"));
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario u = usuarioDAO.buscarPorId(rs.getInt("usuario_id"));
        p.setUsuario(u);
        carregarItens(p);
        return p;
    }

    private void carregarItens(Pedido pedido) throws SQLException {
        String sql = "SELECT * FROM itens_pedido WHERE pedido_id=?";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setInt(1, pedido.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ItemPedido item = new ItemPedido();
                item.setId(rs.getInt("id"));
                item.setPedidoId(rs.getInt("pedido_id"));
                item.setProdutoId(rs.getInt("produto_id"));
                item.setNomeProduto(rs.getString("nome_produto"));
                item.setPrecoUnitario(rs.getDouble("preco_unitario"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setSubtotal(rs.getDouble("subtotal"));
                pedido.adicionarItem(item);
            }
        }
    }
}