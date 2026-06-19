package com.ecommerce.exception;

/**
 * Excecao lancada em caso de falha na autenticacao do usuario.
 */
public class AutenticacaoException extends EcommerceException {

    public AutenticacaoException() {
        super("Email ou senha incorretos.");
    }

    public AutenticacaoException(String mensagem) {
        super(mensagem);
    }
}