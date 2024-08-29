package com.example.appemprestimo.entidades;

import androidx.annotation.NonNull;

public class Parcela {
    private int num;
    private double prestacao;
    private double juros;
    private double amort;
    private double sdDevedor;

    public Parcela(int num, double prestacao, double juros, double amort, double sdDevedor) {
        this.num = num;
        this.prestacao = prestacao;
        this.juros = juros;
        this.amort = amort;
        this.sdDevedor = sdDevedor;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrestacao() {
        return prestacao;
    }

    public void setPrestacao(double prestacao) {
        this.prestacao = prestacao;
    }

    public double getJuros() {
        return juros;
    }

    public void setJuros(double juros) {
        this.juros = juros;
    }

    public double getAmort() {
        return amort;
    }

    public void setAmort(double amort) {
        this.amort = amort;
    }

    public double getSdDevedor() {
        return sdDevedor;
    }

    public void setSdDevedor(double sdDevedor) {
        this.sdDevedor = sdDevedor;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%02d %8.2f %8.2f %8.2f %8.2f", num, prestacao, juros, amort, sdDevedor);
    }
}
