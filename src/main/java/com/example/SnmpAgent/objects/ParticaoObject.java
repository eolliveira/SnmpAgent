package com.example.SnmpAgent.objects;

import java.io.Serializable;

public class ParticaoObject implements Serializable {
    private String pontoMontagem;
    private String capacidade;

    public ParticaoObject(String pontoMontagem, Long tamanho) {
        this.pontoMontagem = pontoMontagem;
        this.capacidade = String.valueOf(tamanho);
    }

    @Override
    public String toString() {
        return "{" +
                "'pontoMontagem':'" + pontoMontagem.substring(0, 2) + '\'' +
                ", 'capacidade':'" + capacidade + '\'' +
                "}";
    }
}
