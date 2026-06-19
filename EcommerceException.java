package com.ecommerce.exception;

/**
 * Excecao base para o sistema de e-commerce.
 * Todas as excecoes customizadas herdam desta classe.
 */
public class EcommerceException extends Exception {

    public EcommerceException(String mensagem) {
        super(mensagem);
    }

    public EcommerceException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}