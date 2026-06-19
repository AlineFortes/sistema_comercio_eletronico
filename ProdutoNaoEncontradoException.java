package com.ecommerce.exception;

/**
 * Excecao lancada quando um produto nao e encontrado no sistema.
 */
public class ProdutoNaoEncontradoException extends EcommerceException {

    public ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public ProdutoNaoEncontradoException(int id) {
        super("Produto com ID " + id + " nao encontrado no sistema.");
    }
}