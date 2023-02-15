package com.example.SnmpAgent.objects;

import java.io.Serializable;

public class ParticaoObject implements Serializable {
    private String pontoMontagem;
    private String capacidade;
    private String usado;

    public ParticaoObject(String pontoMontagem, Long tamanho, Long usado) {
        this.pontoMontagem = pontoMontagem;
        this.capacidade = String.valueOf(tamanho);
        this.usado = String.valueOf(usado);
    }

    @Override
    public String toString() {
        return "{" +
                "'pontoMontagem':'" + pontoMontagem.substring(0, 2) + '\'' +
                ", 'capacidade':'" + capacidade + '\'' +
                ", 'usado':'" + usado + '\'' +
                "}";
    }
}
