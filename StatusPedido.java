package com.ecommerce.enums;

/**
 * Enum que representa os possiveis status de um pedido.
 * Utilizado para controle do fluxo de compra.
 */
public enum StatusPedido {
    PENDENTE("Pendente", "Pedido aguardando confirmacao"),
    PAGO("Pago", "Pagamento confirmado"),
    PROCESSANDO("Processando", "Pedido em preparacao"),
    ENVIADO("Enviado", "Pedido enviado para entrega"),
    ENTREGUE("Entregue", "Pedido entregue ao cliente"),
    CANCELADO("Cancelado", "Pedido cancelado");

    private final String descricao;
    private final String detalhes;

    StatusPedido(String descricao, String detalhes) {
        this.descricao = descricao;
        this.detalhes = detalhes;
    }

    public String getDescricao() { return descricao; }
    public String getDetalhes() { return detalhes; }

    @Override
    public String toString() { return descricao; }
}