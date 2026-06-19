package com.ecommerce.model;

import com.ecommerce.enums.CategoriaProduto;
import com.ecommerce.exception.EstoqueInsuficienteException;
import com.ecommerce.exception.ValidacaoException;

/**
 * Classe que representa um produto no sistema de e-commerce.
 * Aplica ENCAPSULAMENTO com validacoes nos setters.
 * Implementa Comparable para ordenacao natural por nome.
 */
public class Produto implements Comparable<Produto> {

    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private CategoriaProduto categoria;
    private int quantidadeEstoque;
    private String dataCadastro;
    private boolean ativo;

    /**
     * Construtor padrao.
     */
    public Produto() {
        this.ativo = true;
        this.dataCadastro = java.time.LocalDateTime.now().toString();
    }

    /**
     * Construtor completo.
     * 
     * @param id Identificador unico
     * @param nome Nome do produto
     * @param descricao Descricao detalhada
     * @param preco Preco unitario
     * @param categoria Categoria do produto
     * @param quantidadeEstoque Quantidade disponivel
     * @throws ValidacaoException se os dados forem invalidos
     */
    public Produto(int id, String nome, String descricao, double preco, 
                   CategoriaProduto categoria, int quantidadeEstoque) 
            throws ValidacaoException {
        this();
        setId(id);
        setNome(nome);
        setDescricao(descricao);
        setPreco(preco);
        setCategoria(categoria);
        setQuantidadeEstoque(quantidadeEstoque);
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public double getPreco() { return preco; }
    public CategoriaProduto getCategoria() { return categoria; }
    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public String getDataCadastro() { return dataCadastro; }
    public boolean isAtivo() { return ativo; }

    // Setters com validacao
    public void setId(int id) { this.id = id; }

    public void setNome(String nome) throws ValidacaoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("O nome do produto nao pode ser vazio.");
        }
        if (nome.length() < 2 || nome.length() > 100) {
            throw new ValidacaoException("O nome deve ter entre 2 e 100 caracteres.");
        }
        this.nome = nome.trim();
    }

    public void setDescricao(String descricao) throws ValidacaoException {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new ValidacaoException("A descricao nao pode ser vazia.");
        }
        if (descricao.length() < 10 || descricao.length() > 500) {
            throw new ValidacaoException("A descricao deve ter entre 10 e 500 caracteres.");
        }
        this.descricao = descricao.trim();
    }

    public void setPreco(double preco) throws ValidacaoException {
        if (preco <= 0) {
            throw new ValidacaoException("O preco deve ser maior que zero.");
        }
        if (preco > 1000000) {
            throw new ValidacaoException("O preco excede o limite maximo permitido.");
        }
        this.preco = preco;
    }

    public void setCategoria(CategoriaProduto categoria) throws ValidacaoException {
        if (categoria == null) {
            throw new ValidacaoException("A categoria nao pode ser nula.");
        }
        this.categoria = categoria;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) throws ValidacaoException {
        if (quantidadeEstoque < 0) {
            throw new ValidacaoException("A quantidade em estoque nao pode ser negativa.");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    /**
     * Verifica se ha estoque suficiente para uma quantidade solicitada.
     * 
     * @param quantidade Quantidade desejada
     * @return true se houver estoque suficiente
     */
    public boolean temEstoque(int quantidade) {
        return quantidadeEstoque >= quantidade;
    }

    /**
     * Reduz o estoque apos uma venda.
     * 
     * @param quantidade Quantidade a reduzir
     * @throws EstoqueInsuficienteException se nao houver estoque suficiente
     */
    public void reduzirEstoque(int quantidade) throws EstoqueInsuficienteException {
        if (!temEstoque(quantidade)) {
            throw new EstoqueInsuficienteException(nome, quantidadeEstoque, quantidade);
        }
        this.quantidadeEstoque -= quantidade;
    }

    /**
     * Aumenta o estoque (ex: devolucao ou reposicao).
     * 
     * @param quantidade Quantidade a adicionar
     */
    public void aumentarEstoque(int quantidade) {
        if (quantidade > 0) {
            this.quantidadeEstoque += quantidade;
        }
    }

    /**
     * Calcula o valor total para uma quantidade deste produto.
     * 
     * @param quantidade Quantidade
     * @return valor total
     */
    public double calcularValorTotal(int quantidade) {
        return preco * quantidade;
    }

    @Override
    public int compareTo(Produto outro) {
        return this.nome.compareToIgnoreCase(outro.nome);
    }

    @Override
    public String toString() {
        return String.format("Produto[id=%d, nome=%s, preco=%.2f, categoria=%s, estoque=%d, ativo=%s]",
                id, nome, preco, categoria, quantidadeEstoque, ativo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produto produto = (Produto) obj;
        return id == produto.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}