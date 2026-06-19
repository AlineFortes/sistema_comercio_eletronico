package com.ecommerce.model;

import com.ecommerce.exception.EstoqueInsuficienteException;
import com.ecommerce.exception.ValidacaoException;

/**
 * Classe que representa um item no carrinho de compras.
 * Demonstra COMPOSICAO - contem uma referencia a Produto.
 * Cada item associa um produto a uma quantidade desejada.
 */
public class ItemCarrinho {

    private int id;
    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    /**
     * Construtor padrao.
     */
    public ItemCarrinho() {}

    /**
     * Construtor completo.
     * 
     * @param id Identificador do item
     * @param produto Produto associado
     * @param quantidade Quantidade desejada
     * @throws ValidacaoException se a quantidade for invalida
     * @throws EstoqueInsuficienteException se nao houver estoque
     */
    public ItemCarrinho(int id, Produto produto, int quantidade) 
            throws ValidacaoException, EstoqueInsuficienteException {
        this.id = id;
        setProduto(produto);
        setQuantidade(quantidade);
        // Guarda o preco no momento da adicao (evita alteracoes posteriores)
        this.precoUnitario = produto.getPreco();
    }

    // Getters
    public int getId() { return id; }
    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }

    // Setters
    public void setId(int id) { this.id = id; }

    public void setProduto(Produto produto) throws ValidacaoException {
        if (produto == null) {
            throw new ValidacaoException("O produto nao pode ser nulo.");
        }
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) 
            throws ValidacaoException, EstoqueInsuficienteException {
        if (quantidade <= 0) {
            throw new ValidacaoException("A quantidade deve ser maior que zero.");
        }
        if (produto != null && !produto.temEstoque(quantidade)) {
            throw new EstoqueInsuficienteException(
                produto.getNome(), produto.getQuantidadeEstoque(), quantidade);
        }
        this.quantidade = quantidade;
        if (produto != null) {
            this.precoUnitario = produto.getPreco();
        }
    }

    /**
     * Calcula o subtotal deste item (preco * quantidade).
     * 
     * @return subtotal
     */
    public double getSubtotal() {
        return precoUnitario * quantidade;
    }

    /**
     * Aumenta a quantidade do item.
     * 
     * @param quantidadeAdicional Quantidade a adicionar
     * @throws EstoqueInsuficienteException se nao houver estoque
     * @throws ValidacaoException se a quantidade for invalida
     */
    public void aumentarQuantidade(int quantidadeAdicional) 
            throws EstoqueInsuficienteException, ValidacaoException {
        int novaQuantidade = this.quantidade + quantidadeAdicional;
        setQuantidade(novaQuantidade);
    }

    /**
     * Diminui a quantidade do item.
     * 
     * @param quantidadeReduzir Quantidade a remover
     * @return true se o item ainda existe (quantidade > 0), false se deve ser removido
     * @throws ValidacaoException se a quantidade for invalida
     */
    public boolean diminuirQuantidade(int quantidadeReduzir) throws ValidacaoException {
        if (quantidadeReduzir <= 0) {
            throw new ValidacaoException("A quantidade a reduzir deve ser maior que zero.");
        }
        this.quantidade -= quantidadeReduzir;
        return this.quantidade > 0;
    }

    @Override
    public String toString() {
        return String.format("ItemCarrinho[produto=%s, qtd=%d, precoUnit=%.2f, subtotal=%.2f]",
                produto.getNome(), quantidade, precoUnitario, getSubtotal());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemCarrinho item = (ItemCarrinho) obj;
        return produto != null && produto.equals(item.produto);
    }

    @Override
    public int hashCode() {
        return produto != null ? produto.hashCode() : 0;
    }
}