package com.example.appemprestimo.util;

public class Price {
    static public double calcParcela(double valor, double juros, int prazo) {
        return valor * (juros / 100 / (1 - Math.pow(1 + juros / 100, prazo * -1)));
    }
}
