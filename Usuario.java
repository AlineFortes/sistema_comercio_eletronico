package com.ecommerce.model;

import com.ecommerce.enums.TipoUsuario;
import com.ecommerce.exception.ValidacaoException;
import com.ecommerce.security.Seguranca;

/**
 * Classe que representa um usuario do sistema.
 * Demonstra HERANCA - estende Pessoa.
 * Aplica ENCAPSULAMENTO - senha armazenada de forma segura (hash).
 */
public class Usuario extends Pessoa {

    private String senhaHash;
    private TipoUsuario tipo;
    private boolean ativo;
    private String dataCadastro;

    /**
     * Construtor padrao.
     */
    public Usuario() {
        this.tipo = TipoUsuario.CLIENTE;
        this.ativo = true;
        this.dataCadastro = java.time.LocalDateTime.now().toString();
    }

    /**
     * Construtor completo.
     */
    public Usuario(int id, String nome, String email, String senha, String endereco, 
                   String telefone, TipoUsuario tipo) throws ValidacaoException {
        super(id, nome, email, endereco, telefone);
        setSenha(senha);
        this.tipo = tipo != null ? tipo : TipoUsuario.CLIENTE;
        this.ativo = true;
        this.dataCadastro = java.time.LocalDateTime.now().toString();
    }

    // Getters
    public String getSenhaHash() { return senhaHash; }
    public TipoUsuario getTipo() { return tipo; }
    public boolean isAtivo() { return ativo; }
    public String getDataCadastro() { return dataCadastro; }

    // Setters
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }

    /**
     * Define a senha do usuario, armazenando apenas o hash.
     * Seguranca: nunca armazena a senha em texto plano.
     * 
     * @param senha Senha em texto plano
     * @throws ValidacaoException se a senha for muito curta
     */
    public void setSenha(String senha) throws ValidacaoException {
        if (senha == null || senha.length() < 6) {
            throw new ValidacaoException("A senha deve ter pelo menos 6 caracteres.");
        }
        this.senhaHash = Seguranca.hashSenha(senha);
    }

    /**
     * Define o hash da senha diretamente (usado ao carregar do banco).
     * 
     * @param senhaHash Hash da senha
     */
    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    /**
     * Verifica se a senha informada corresponde ao hash armazenado.
     * 
     * @param senha Senha em texto plano
     * @return true se a senha estiver correta
     */
    public boolean verificarSenha(String senha) {
        return Seguranca.verificarSenha(senha, this.senhaHash);
    }

    /**
     * Verifica se o usuario tem permissao de administrador.
     * 
     * @return true se for administrador
     */
    public boolean isAdministrador() {
        return tipo == TipoUsuario.ADMINISTRADOR;
    }

    @Override
    public String getTipo() {
        return tipo.getNome();
    }

    @Override
    public String toString() {
        return String.format("Usuario[id=%d, nome=%s, email=%s, tipo=%s, ativo=%s, cadastro=%s]",
                getId(), getNome(), getEmail(), tipo, ativo, dataCadastro);
    }
}