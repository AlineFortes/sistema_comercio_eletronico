package com.ecommerce.controller;

import com.ecommerce.enums.TipoUsuario;
import com.ecommerce.exception.AutenticacaoException;
import com.ecommerce.exception.UsuarioNaoEncontradoException;
import com.ecommerce.exception.ValidacaoException;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.UsuarioService;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller para Usuario.
 */
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }

    public void cadastrar(String nome, String email, String senha, String endereco, 
                          String telefone, TipoUsuario tipo) 
            throws ValidacaoException, SQLException {
        Usuario u = new Usuario(0, nome, email, senha, endereco, telefone, tipo);
        usuarioService.cadastrar(u);
    }

    public void atualizar(int id, String nome, String email, String endereco, 
                          String telefone, TipoUsuario tipo, boolean ativo) 
            throws ValidacaoException, UsuarioNaoEncontradoException, SQLException {
        Usuario u = usuarioService.buscarPorId(id);
        u.setNome(nome);
        u.setEmail(email);
        u.setEndereco(endereco);
        u.setTelefone(telefone);
        u.setTipo(tipo);
        u.setAtivo(ativo);
        usuarioService.atualizar(u);
    }

    public void excluir(int id) throws UsuarioNaoEncontradoException, SQLException {
        usuarioService.excluir(id);
    }

    public Usuario autenticar(String email, String senha) throws AutenticacaoException, SQLException {
        return usuarioService.autenticar(email, senha);
    }

    public Usuario buscarPorId(int id) throws UsuarioNaoEncontradoException, SQLException {
        return usuarioService.buscarPorId(id);
    }

    public List<Usuario> listarTodos() throws SQLException {
        return usuarioService.listarTodos();
    }
}