package com.ecommerce.model;

/**
 * Classe que representa um item dentro de um pedido ja finalizado.
 * Diferente de ItemCarrinho, este guarda os dados no momento da compra
 * para historico (imutabilidade do pedido).
 * Demonstra COMPOSICAO com Pedido.
 */
public class ItemPedido {

    private int id;
    private int pedidoId;
    private int produtoId;
    private String nomeProduto;
    private double precoUnitario;
    private int quantidade;
    private double subtotal;

    /**
     * Construtor padrao.
     */
    public ItemPedido() {}

    /**
     * Construtor a partir de um ItemCarrinho.
     * Copia os dados no momento da compra para garantir historico.
     * 
     * @param id Identificador
     * @param pedidoId ID do pedido pai
     * @param itemCarrinho Item do carrinho a converter
     */
    public ItemPedido(int id, int pedidoId, ItemCarrinho itemCarrinho) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.produtoId = itemCarrinho.getProduto().getId();
        this.nomeProduto = itemCarrinho.getProduto().getNome();
        this.precoUnitario = itemCarrinho.getPrecoUnitario();
        this.quantidade = itemCarrinho.getQuantidade();
        this.subtotal = itemCarrinho.getSubtotal();
    }

    /**
     * Construtor completo.
     */
    public ItemPedido(int id, int pedidoId, int produtoId, String nomeProduto, 
                      double precoUnitario, int quantidade, double subtotal) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    // Getters
    public int getId() { return id; }
    public int getPedidoId() { return pedidoId; }
    public int getProdutoId() { return produtoId; }
    public String getNomeProduto() { return nomeProduto; }
    public double getPrecoUnitario() { return precoUnitario; }
    public int getQuantidade() { return quantidade; }
    public double getSubtotal() { return subtotal; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }
    public void setProdutoId(int produtoId) { this.produtoId = produtoId; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    @Override
    public String toString() {
        return String.format("ItemPedido[id=%d, produto=%s, qtd=%d, preco=%.2f, subtotal=%.2f]",
                id, nomeProduto, quantidade, precoUnitario, subtotal);
    }
}