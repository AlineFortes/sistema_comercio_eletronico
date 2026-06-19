package com.ecommerce.dao;

import com.ecommerce.enums.CategoriaProduto;
import com.ecommerce.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para Produto.
 */
public class ProdutoDAO {

    private Connection getConexao() throws SQLException {
        return ConexaoBD.getConexao();
    }

    public void inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, descricao, preco, categoria, quantidade_estoque, ativo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getCategoria().name());
            stmt.setInt(5, produto.getQuantidadeEstoque());
            stmt.setInt(6, produto.isAtivo() ? 1 : 0);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) produto.setId(rs.getInt(1));
            getConexao().commit();
        }
    }

    public void atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET nome=?, descricao=?, preco=?, categoria=?, quantidade_estoque=?, ativo=? WHERE id=?";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getCategoria().name());
            stmt.setInt(5, produto.getQuantidadeEstoque());
            stmt.setInt(6, produto.isAtivo() ? 1 : 0);
            stmt.setInt(7, produto.getId());
            stmt.executeUpdate();
            getConexao().commit();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id=?";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            getConexao().commit();
        }
    }

    public Produto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM produtos WHERE id=?";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapearProduto(rs);
        }
        return null;
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE ativo=1 ORDER BY nome";
        try (Statement stmt = getConexao().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) produtos.add(mapearProduto(rs));
        }
        return produtos;
    }

    public List<Produto> buscarPorNome(String nome) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE ativo=1 AND nome LIKE ? ORDER BY nome";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) produtos.add(mapearProduto(rs));
        }
        return produtos;
    }

    public List<Produto> buscarPorCategoria(CategoriaProduto categoria) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE ativo=1 AND categoria=? ORDER BY nome";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setString(1, categoria.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) produtos.add(mapearProduto(rs));
        }
        return produtos;
    }

    private Produto mapearProduto(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        try {
            p.setId(rs.getInt("id"));
            p.setNome(rs.getString("nome"));
            p.setDescricao(rs.getString("descricao"));
            p.setPreco(rs.getDouble("preco"));
            p.setCategoria(CategoriaProduto.valueOf(rs.getString("categoria")));
            p.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
            p.setAtivo(rs.getInt("ativo") == 1);
            p.setDataCadastro(rs.getString("data_cadastro"));
        } catch (Exception e) {
            throw new SQLException("Erro ao mapear produto: " + e.getMessage());
        }
        return p;
    }
}