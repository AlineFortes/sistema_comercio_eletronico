package com.ecommerce.security;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe utilitaria para seguranca do sistema.
 * Responsavel pelo hash e verificacao de senhas usando BCrypt.
 * Aplica ENCAPSULAMENTO - metodos estaticos utilitarios.
 */
public class Seguranca {

    private static final int BCRYPT_ROUNDS = 12;

    private Seguranca() {}

    public static String hashSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    public static boolean verificarSenha(String senha, String hash) {
        if (senha == null || hash == null) return false;
        return BCrypt.checkpw(senha, hash);
    }

    public static String gerarToken() {
        return java.util.UUID.randomUUID().toString();
    }
}