package com.example.appmynote.entidades;

public class Note {
    private final String titulo;
    private final String prioridade;
    private final String conteudo;
    private final String  caminho;


    public Note(String titulo, String prioridade, String conteudo, String caminho) {
        this.titulo = titulo;
        this.prioridade = prioridade;
        this.conteudo = conteudo;
        this.caminho = caminho;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public String getConteudo() {
        return conteudo;
    }


    public String getCaminho() {
        return caminho;
    }

}
