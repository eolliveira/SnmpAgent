package com.example.SnmpAgent.objects;

import java.io.Serializable;

public class ParticaoObject implements Serializable {
    private String pontoMontagem;
    private String tamanho;

    public ParticaoObject(String pontoMontagem, Long tamanho) {
        this.pontoMontagem = pontoMontagem;
        this.tamanho = String.valueOf(tamanho);
    }

    @Override
    public String toString() {
        return "{" +
                "'pontoMontagem':'" + pontoMontagem.substring(0, 2) + '\'' +
                ", 'tamanho':'" + tamanho + '\'' +
                "}";
    }
}
