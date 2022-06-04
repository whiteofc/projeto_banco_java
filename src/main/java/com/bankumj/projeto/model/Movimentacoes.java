package com.bankumj.projeto.model;

public class Movimentacoes {
    private String data;
    private String nomeDestinatario;
    private String valorTransacao;

    public Movimentacoes(String data, String nomeDestinatario, String valorTransacao) {
        this.data = data;
        this.nomeDestinatario = nomeDestinatario;
        this.valorTransacao = valorTransacao;

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNomeDestinatario() {
        return nomeDestinatario;
    }

    public void setNomeDestinatario(String nomeDestinatario) {
        this.nomeDestinatario = nomeDestinatario;
    }

    public String getValorTransacao() {
        return valorTransacao;
    }

    public void setValorTransacao(String valorTransacao) {
        this.valorTransacao = valorTransacao;
    }
}