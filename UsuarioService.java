package com.ecommerce.service;

import com.ecommerce.dao.UsuarioDAO;
import com.ecommerce.exception.AutenticacaoException;
import com.ecommerce.exception.UsuarioNaoEncontradoException;
import com.ecommerce.exception.ValidacaoException;
import com.ecommerce.model.Usuario;

import java.sql.SQLException;
import java.util.List;

/**
 * Service para Usuario.
 */
public class UsuarioService {

    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public void cadastrar(Usuario usuario) throws ValidacaoException, SQLException {
        if (usuario == null) throw new ValidacaoException("Usuario nao pode ser nulo.");
        if (usuarioDAO.buscarPorEmail(usuario.getEmail()) != null) {
            throw new ValidacaoException("Email ja cadastrado no sistema.");
        }
        usuarioDAO.inserir(usuario);
    }

    public void atualizar(Usuario usuario) throws ValidacaoException, UsuarioNaoEncontradoException, SQLException {
        if (usuario == null || usuario.getId() <= 0) throw new ValidacaoException("Usuario invalido.");
        if (usuarioDAO.buscarPorId(usuario.getId()) == null) throw new UsuarioNaoEncontradoException(usuario.getId());
        usuarioDAO.atualizar(usuario);
    }

    public void excluir(int id) throws UsuarioNaoEncontradoException, SQLException {
        if (usuarioDAO.buscarPorId(id) == null) throw new UsuarioNaoEncontradoException(id);
        usuarioDAO.excluir(id);
    }

    public Usuario autenticar(String email, String senha) throws AutenticacaoException, SQLException {
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario == null || !usuario.verificarSenha(senha)) {
            throw new AutenticacaoException();
        }
        if (!usuario.isAtivo()) {
            throw new AutenticacaoException("Usuario desativado. Contate o administrador.");
        }
        return usuario;
    }

    public Usuario buscarPorId(int id) throws UsuarioNaoEncontradoException, SQLException {
        Usuario u = usuarioDAO.buscarPorId(id);
        if (u == null) throw new UsuarioNaoEncontradoException(id);
        return u;
    }

    public List<Usuario> listarTodos() throws SQLException {
        return usuarioDAO.listarTodos();
    }
}