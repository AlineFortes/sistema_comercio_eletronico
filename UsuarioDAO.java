package com.ecommerce.dao;

import com.ecommerce.enums.TipoUsuario;
import com.ecommerce.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para Usuario.
 */
public class UsuarioDAO {

    private Connection getConexao() throws SQLException {
        return ConexaoBD.getConexao();
    }

    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha_hash, endereco, telefone, tipo, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenhaHash());
            stmt.setString(4, usuario.getEndereco());
            stmt.setString(5, usuario.getTelefone());
            stmt.setString(6, usuario.getTipo().name());
            stmt.setInt(7, usuario.isAtivo() ? 1 : 0);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) usuario.setId(rs.getInt(1));
            getConexao().commit();
        }
    }

    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome=?, email=?, endereco=?, telefone=?, tipo=?, ativo=? WHERE id=?";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getEndereco());
            stmt.setString(4, usuario.getTelefone());
            stmt.setString(5, usuario.getTipo().name());
            stmt.setInt(6, usuario.isAtivo() ? 1 : 0);
            stmt.setInt(7, usuario.getId());
            stmt.executeUpdate();
            getConexao().commit();
        }
    }

    public void atualizarSenha(int id, String novaSenhaHash) throws SQLException {
        String sql = "UPDATE usuarios SET senha_hash=? WHERE id=?";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setString(1, novaSenhaHash);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            getConexao().commit();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id=?";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            getConexao().commit();
        }
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id=?";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapearUsuario(rs);
        }
        return null;
    }

    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email=?";
        try (PreparedStatement stmt = getConexao().prepareStatement(sql)) {
            stmt.setString(1, email.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapearUsuario(rs);
        }
        return null;
    }

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        try (Statement stmt = getConexao().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) usuarios.add(mapearUsuario(rs));
        }
        return usuarios;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        try {
            u.setNome(rs.getString("nome"));
            u.setEmail(rs.getString("email"));
        } catch (Exception e) {}
        u.setSenhaHash(rs.getString("senha_hash"));
        try {
            u.setEndereco(rs.getString("endereco"));
            u.setTelefone(rs.getString("telefone"));
        } catch (Exception e) {}
        u.setTipo(TipoUsuario.valueOf(rs.getString("tipo")));
        u.setAtivo(rs.getInt("ativo") == 1);
        u.setDataCadastro(rs.getString("data_cadastro"));
        return u;
    }
}