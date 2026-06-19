package com.ecommerce.service;

import com.ecommerce.exception.CarrinhoVazioException;
import com.ecommerce.exception.EstoqueInsuficienteException;
import com.ecommerce.exception.ValidacaoException;
import com.ecommerce.model.Carrinho;
import com.ecommerce.model.Produto;
import com.ecommerce.model.Usuario;

import java.util.HashMap;
import java.util.Map;

/**
 * Service para Carrinho.
 * Gerencia carrinhos em memoria (um por usuario logado).
 */
public class CarrinhoService {

    private Map<Integer, Carrinho> carrinhos;

    public CarrinhoService() {
        this.carrinhos = new HashMap<>();
    }

    public Carrinho getCarrinho(Usuario usuario) {
        return carrinhos.computeIfAbsent(usuario.getId(), k -> new Carrinho(0, usuario));
    }

    public void adicionarProduto(Usuario usuario, Produto produto, int quantidade) 
            throws ValidacaoException, EstoqueInsuficienteException {
        getCarrinho(usuario).adicionarItem(produto, quantidade);
    }

    public void removerProduto(Usuario usuario, Produto produto) {
        getCarrinho(usuario).removerItem(produto);
    }

    public void alterarQuantidade(Usuario usuario, Produto produto, int novaQuantidade) 
            throws ValidacaoException, EstoqueInsuficienteException {
        getCarrinho(usuario).alterarQuantidade(produto, novaQuantidade);
    }

    public void limparCarrinho(Usuario usuario) {
        getCarrinho(usuario).limpar();
    }

    public void validarParaFinalizar(Usuario usuario) throws CarrinhoVazioException {
        getCarrinho(usuario).validarParaFinalizar();
    }

    public double getTotal(Usuario usuario) {
        return getCarrinho(usuario).getTotal();
    }

    public int getQuantidadeItens(Usuario usuario) {
        return getCarrinho(usuario).getQuantidadeTotalItens();
    }
}