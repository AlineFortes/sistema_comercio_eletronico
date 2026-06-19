package com.ecommerce.enums;

/**
 * Enum que representa as categorias de produtos disponiveis.
 * Facilita a organizacao e filtragem de produtos.
 */
public enum CategoriaProduto {
    ELETRONICOS("Eletronicos", "Dispositivos eletronicos e acessorios"),
    INFORMATICA("Informatica", "Computadores, notebooks e perifericos"),
    CELULARES("Celulares", "Smartphones e acessorios"),
    AUDIO("Audio", "Fones de ouvido, caixas de som e equipamentos de audio"),
    GAMES("Games", "Consoles, jogos e acessorios"),
    FOTOGRAFIA("Fotografia", "Cameras e equipamentos fotograficos"),
    ACESSORIOS("Acessorios", "Cabos, carregadores e outros acessorios"),
    SMART_HOME("Smart Home", "Dispositivos inteligentes para casa");

    private final String nome;
    private final String descricao;

    CategoriaProduto(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }

    @Override
    public String toString() { return nome; }
}