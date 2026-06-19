package com.ecommerce.enums;

/**
 * Enum que define os tipos de usuarios do sistema.
 * Controla as permissoes de acesso.
 */
public enum TipoUsuario {
    ADMINISTRADOR("Administrador", "Acesso total ao sistema"),
    CLIENTE("Cliente", "Acesso limitado para compras");

    private final String nome;
    private final String descricao;

    TipoUsuario(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }

    @Override
    public String toString() { return nome; }
}