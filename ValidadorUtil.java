package com.ecommerce.util;

/**
 * Classe utilitaria para validacoes comuns.
 * Aplica o principio DRY (Don't Repeat Yourself).
 */
public class ValidadorUtil {

    private ValidadorUtil() {}

    public static boolean isVazioOuNulo(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public static boolean isEmailValido(String email) {
        if (isVazioOuNulo(email)) return false;
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }

    public static boolean isTelefoneValido(String telefone) {
        if (isVazioOuNulo(telefone)) return false;
        String limpo = telefone.replaceAll("[^0-9]", "");
        return limpo.length() >= 7 && limpo.length() <= 15;
    }
}