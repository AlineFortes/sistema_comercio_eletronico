package com.ecommerce.exception;

/**
 * Excecao lancada quando o usuario tenta finalizar compra com carrinho vazio.
 */
public class CarrinhoVazioException extends EcommerceException {

    public CarrinhoVazioException() {
        super("O carrinho de compras esta vazio. Adicione produtos antes de finalizar a compra.");
    }

    public CarrinhoVazioException(String mensagem) {
        super(mensagem);
    }
}