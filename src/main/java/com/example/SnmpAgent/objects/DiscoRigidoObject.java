package com.example.SnmpAgent.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiscoRigidoObject implements Serializable {
    private String nome;
    private String modelo;
    private String numeroSerie;
    private String capacidade;

    private List<ParticaoObject> particoes = new ArrayList<>();

    public DiscoRigidoObject(){}

    public DiscoRigidoObject(String name, String model, String serial, long size) {
        this.nome = name;
        this.modelo = model;
        this.numeroSerie = serial;
        this.capacidade = String.valueOf(size);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(String capacidade) {
        this.capacidade = capacidade;
    }

    public List<ParticaoObject> getParticoes() {
        return particoes;
    }

    public void setParticoes(List<ParticaoObject> particoes) {
        this.particoes = particoes;
    }

    @Override
    public String toString() {
        return "{" +
                "'nome':'" + nome + '\'' +
                ", 'modelo':'" + modelo + '\'' +
                ", 'numeroSerie':'" + numeroSerie + '\'' +
                ", 'capacidade':'" + capacidade + '\'' +
                ", 'particoes':" + particoes +
                "}";
    }

}
