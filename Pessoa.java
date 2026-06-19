package com.ecommerce.model;

import com.ecommerce.exception.ValidacaoException;

/**
 * Classe abstrata que representa uma pessoa no sistema.
 * Demonstra o conceito de HERANCA - serve como base para Usuario e outras entidades.
 * Aplica ENCAPSULAMENTO com atributos privados e getters/setters.
 */
public abstract class Pessoa {

    private int id;
    private String nome;
    private String email;
    private String endereco;
    private String telefone;

    /**
     * Construtor padrao.
     */
    public Pessoa() {}

    /**
     * Construtor completo para inicializacao de uma pessoa.
     * 
     * @param id Identificador unico
     * @param nome Nome completo
     * @param email Endereco de email
     * @param endereco Endereco fisico
     * @param telefone Numero de telefone
     * @throws ValidacaoException se os dados forem invalidos
     */
    public Pessoa(int id, String nome, String email, String endereco, String telefone) 
            throws ValidacaoException {
        this.id = id;
        setNome(nome);
        setEmail(email);
        setEndereco(endereco);
        setTelefone(telefone);
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getEndereco() { return endereco; }
    public String getTelefone() { return telefone; }

    // Setters com validacao (encapsulamento)
    public void setId(int id) { this.id = id; }

    public void setNome(String nome) throws ValidacaoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("O nome nao pode ser vazio.");
        }
        if (nome.length() < 2 || nome.length() > 100) {
            throw new ValidacaoException("O nome deve ter entre 2 e 100 caracteres.");
        }
        this.nome = nome.trim();
    }

    public void setEmail(String email) throws ValidacaoException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidacaoException("O email nao pode ser vazio.");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new ValidacaoException("O formato do email e invalido.");
        }
        this.email = email.trim().toLowerCase();
    }

    public void setEndereco(String endereco) throws ValidacaoException {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new ValidacaoException("O endereco nao pode ser vazio.");
        }
        if (endereco.length() < 5 || endereco.length() > 200) {
            throw new ValidacaoException("O endereco deve ter entre 5 e 200 caracteres.");
        }
        this.endereco = endereco.trim();
    }

    public void setTelefone(String telefone) throws ValidacaoException {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new ValidacaoException("O telefone nao pode ser vazio.");
        }
        // Aceita formatos: +238 123 4567, 91234567, etc.
        String telLimpo = telefone.replaceAll("[^0-9]", "");
        if (telLimpo.length() < 7 || telLimpo.length() > 15) {
            throw new ValidacaoException("O telefone deve ter entre 7 e 15 digitos.");
        }
        this.telefone = telefone.trim();
    }

    /**
     * Metodo abstrato que deve ser implementado pelas subclasses.
     * Define o tipo de pessoa no sistema.
     * 
     * @return descricao do tipo de pessoa
     */
    public abstract String getTipo();

    @Override
    public String toString() {
        return String.format("Pessoa[id=%d, nome=%s, email=%s, endereco=%s, telefone=%s]",
                id, nome, email, endereco, telefone);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pessoa pessoa = (Pessoa) obj;
        return id == pessoa.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}