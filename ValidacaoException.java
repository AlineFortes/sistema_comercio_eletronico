package com.ecommerce.exception;

/**
 * Excecao lancada quando os dados nao passam na validacao.
 */
public class ValidacaoException extends EcommerceException {

    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}