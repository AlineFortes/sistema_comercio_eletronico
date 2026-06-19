package com.ecommerce.exception;

/**
 * Excecao lancada quando nao ha estoque suficiente para um produto.
 */
public class EstoqueInsuficienteException extends EcommerceException {

    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }

    public EstoqueInsuficienteException(String nomeProduto, int disponivel, int solicitado) {
        super("Estoque insuficiente para '" + nomeProduto + "'. Disponivel: " + disponivel + 
              ", Solicitado: " + solicitado);
    }
}