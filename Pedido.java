package com.ecommerce.model;

import com.ecommerce.enums.StatusPedido;
import com.ecommerce.exception.ValidacaoException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe que representa um pedido finalizado no sistema.
 * Demonstra COMPOSICAO - contem uma lista de ItemPedido.
 * Demonstra AGREGACAO - referencia um Usuario.
 */
public class Pedido {

    private int id;
    private Usuario usuario;
    private List<ItemPedido> itens;
    private double valorTotal;
    private String dataPedido;
    private StatusPedido status;
    private String enderecoEntrega;

    public Pedido() {
        this.itens = new ArrayList<>();
        this.status = StatusPedido.PENDENTE;
        this.dataPedido = java.time.LocalDateTime.now().toString();
    }

    public Pedido(int id, Usuario usuario, Carrinho carrinho, String enderecoEntrega) 
            throws ValidacaoException {
        this();
        this.id = id;
        this.usuario = usuario;
        this.enderecoEntrega = enderecoEntrega;

        int itemId = 1;
        for (ItemCarrinho itemCarrinho : carrinho.getItens()) {
            ItemPedido itemPedido = new ItemPedido(itemId++, id, itemCarrinho);
            this.itens.add(itemPedido);
        }
        calcularTotal();
    }

    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public List<ItemPedido> getItens() { return Collections.unmodifiableList(itens); }
    public double getValorTotal() { return valorTotal; }
    public String getDataPedido() { return dataPedido; }
    public StatusPedido getStatus() { return status; }
    public String getEnderecoEntrega() { return enderecoEntrega; }

    public void setId(int id) { this.id = id; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setDataPedido(String dataPedido) { this.dataPedido = dataPedido; }
    public void setEnderecoEntrega(String enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public void setStatus(StatusPedido status) throws ValidacaoException {
        if (status == null) {
            throw new ValidacaoException("O status nao pode ser nulo.");
        }
        this.status = status;
    }

    private void calcularTotal() {
        this.valorTotal = 0.0;
        for (ItemPedido item : itens) {
            this.valorTotal += item.getSubtotal();
        }
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        calcularTotal();
    }

    public int getQuantidadeTotal() {
        int total = 0;
        for (ItemPedido item : itens) {
            total += item.getQuantidade();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Pedido[id=%d, usuario=%s, itens=%d, total=%.2f, status=%s, data=%s]",
                id, usuario != null ? usuario.getNome() : "N/A", 
                itens.size(), valorTotal, status, dataPedido);
    }
}