package com.ecommerce.exception;

/**
 * Excecao lancada quando um usuario nao e encontrado no sistema.
 */
public class UsuarioNaoEncontradoException extends EcommerceException {

    public UsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public UsuarioNaoEncontradoException(int id) {
        super("Usuario com ID " + id + " nao encontrado no sistema.");
    }
}