package com.ecommerce.controller;

import com.ecommerce.exception.CarrinhoVazioException;
import com.ecommerce.exception.EstoqueInsuficienteException;
import com.ecommerce.exception.ValidacaoException;
import com.ecommerce.model.Carrinho;
import com.ecommerce.model.Produto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.CarrinhoService;

/**
 * Controller para Carrinho.
 */
public class CarrinhoController {

    private CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    public void adicionarProduto(Usuario usuario, Produto produto, int quantidade) 
            throws ValidacaoException, EstoqueInsuficienteException {
        carrinhoService.adicionarProduto(usuario, produto, quantidade);
    }

    public void removerProduto(Usuario usuario, Produto produto) {
        carrinhoService.removerProduto(usuario, produto);
    }

    public void alterarQuantidade(Usuario usuario, Produto produto, int novaQuantidade) 
            throws ValidacaoException, EstoqueInsuficienteException {
        carrinhoService.alterarQuantidade(usuario, produto, novaQuantidade);
    }

    public void limparCarrinho(Usuario usuario) {
        carrinhoService.limparCarrinho(usuario);
    }

    public Carrinho getCarrinho(Usuario usuario) {
        return carrinhoService.getCarrinho(usuario);
    }

    public double getTotal(Usuario usuario) {
        return carrinhoService.getTotal(usuario);
    }

    public void validarParaFinalizar(Usuario usuario) throws CarrinhoVazioException {
        carrinhoService.validarParaFinalizar(usuario);
    }
}