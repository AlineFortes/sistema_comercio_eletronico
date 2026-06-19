package com.ecommerce.model;

import com.ecommerce.exception.CarrinhoVazioException;
import com.ecommerce.exception.EstoqueInsuficienteException;
import com.ecommerce.exception.ValidacaoException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe que representa o carrinho de compras de um usuario.
 * Demonstra COMPOSICAO - contem uma lista de ItemCarrinho.
 * Demonstra AGREGACAO - referencia um Usuario (pode existir sem o carrinho).
 * Aplica o padrao de projeto Singleton-like por usuario (um carrinho por usuario).
 */
public class Carrinho {

    private int id;
    private Usuario usuario;
    private List<ItemCarrinho> itens;
    private String dataCriacao;

    /**
     * Construtor padrao.
     */
    public Carrinho() {
        this.itens = new ArrayList<>();
        this.dataCriacao = java.time.LocalDateTime.now().toString();
    }

    /**
     * Construtor com usuario.
     * 
     * @param id Identificador do carrinho
     * @param usuario Usuario dono do carrinho
     */
    public Carrinho(int id, Usuario usuario) {
        this();
        this.id = id;
        this.usuario = usuario;
    }

    // Getters
    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public List<ItemCarrinho> getItens() { 
        return Collections.unmodifiableList(itens); 
    }
    public String getDataCriacao() { return dataCriacao; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    /**
     * Adiciona um produto ao carrinho.
     * Se o produto ja existir, aumenta a quantidade.
     * 
     * @param produto Produto a adicionar
     * @param quantidade Quantidade desejada
     * @throws ValidacaoException se dados invalidos
     * @throws EstoqueInsuficienteException se sem estoque
     */
    public void adicionarItem(Produto produto, int quantidade) 
            throws ValidacaoException, EstoqueInsuficienteException {

        // Verifica se o produto ja esta no carrinho
        for (ItemCarrinho item : itens) {
            if (item.getProduto().equals(produto)) {
                item.aumentarQuantidade(quantidade);
                return;
            }
        }

        // Cria novo item
        ItemCarrinho novoItem = new ItemCarrinho(0, produto, quantidade);
        itens.add(novoItem);
    }

    /**
     * Remove um item do carrinho pelo produto.
     * 
     * @param produto Produto a remover
     * @return true se removido com sucesso
     */
    public boolean removerItem(Produto produto) {
        return itens.removeIf(item -> item.getProduto().equals(produto));
    }

    /**
     * Remove um item do carrinho por indice.
     * 
     * @param indice Indice do item
     * @return true se removido com sucesso
     */
    public boolean removerItem(int indice) {
        if (indice >= 0 && indice < itens.size()) {
            itens.remove(indice);
            return true;
        }
        return false;
    }

    /**
     * Altera a quantidade de um item.
     * 
     * @param produto Produto a alterar
     * @param novaQuantidade Nova quantidade
     * @throws ValidacaoException se quantidade invalida
     * @throws EstoqueInsuficienteException se sem estoque
     */
    public void alterarQuantidade(Produto produto, int novaQuantidade) 
            throws ValidacaoException, EstoqueInsuficienteException {
        for (ItemCarrinho item : itens) {
            if (item.getProduto().equals(produto)) {
                item.setQuantidade(novaQuantidade);
                return;
            }
        }
        throw new ValidacaoException("Produto nao encontrado no carrinho.");
    }

    /**
     * Calcula o total do carrinho.
     * 
     * @return valor total
     */
    public double getTotal() {
        double total = 0.0;
        for (ItemCarrinho item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Retorna a quantidade total de itens (soma das quantidades).
     * 
     * @return quantidade total
     */
    public int getQuantidadeTotalItens() {
        int total = 0;
        for (ItemCarrinho item : itens) {
            total += item.getQuantidade();
        }
        return total;
    }

    /**
     * Verifica se o carrinho esta vazio.
     * 
     * @return true se vazio
     */
    public boolean isVazio() {
        return itens.isEmpty();
    }

    /**
     * Limpa todos os itens do carrinho.
     */
    public void limpar() {
        itens.clear();
    }

    /**
     * Verifica se o carrinho pode ser finalizado.
     * 
     * @throws CarrinhoVazioException se estiver vazio
     */
    public void validarParaFinalizar() throws CarrinhoVazioException {
        if (isVazio()) {
            throw new CarrinhoVazioException();
        }
        // Verifica estoque de todos os itens
        for (ItemCarrinho item : itens) {
            if (!item.getProduto().temEstoque(item.getQuantidade())) {
                throw new EstoqueInsuficienteException(
                    item.getProduto().getNome(), 
                    item.getProduto().getQuantidadeEstoque(), 
                    item.getQuantidade());
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Carrinho[id=%d, usuario=%s, itens=%d, total=%.2f]",
                id, usuario != null ? usuario.getNome() : "N/A", itens.size(), getTotal());
    }
}