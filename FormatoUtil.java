package com.ecommerce.util;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitaria para formatacao de valores.
 */
public class FormatoUtil {

    private static final DecimalFormat MOEDA = new DecimalFormat("#,##0.00");
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private FormatoUtil() {}

    public static String formatarMoeda(double valor) {
        return "CVE " + MOEDA.format(valor);
    }

    public static String formatarData(String dataISO) {
        try {
            LocalDateTime data = LocalDateTime.parse(dataISO.substring(0, 19));
            return data.format(DATA);
        } catch (Exception e) {
            return dataISO;
        }
    }

    public static String formatarData(LocalDateTime data) {
        return data.format(DATA);
    }
}