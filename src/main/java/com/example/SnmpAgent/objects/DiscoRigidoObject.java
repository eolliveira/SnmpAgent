package com.example.SnmpAgent.objects;

import java.io.Serializable;

public class DiscoRigidoObject implements Serializable {
    private String nome;
    private String modelo;
    private String numeroSerie;
    private String capacidade;
    private String usado;
    private String disponivel;

    public DiscoRigidoObject(){}

    public DiscoRigidoObject(String name, String model, String serial, long size, long reads, long writes) {
        this.nome = name;
        this.modelo = model;
        this.numeroSerie = serial;
        this.capacidade = String.valueOf(size);
        this.usado = String.valueOf(reads);
        this.disponivel = String.valueOf(writes);
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

    public String getUsado() {
        return usado;
    }

    public void setUsado(String usado) {
        this.usado = usado;
    }

    public String getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(String disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {

        return "{" +
                "'nome':'" + nome + '\'' +
                ", 'modelo':'" + "modelo" + '\'' +
                ", 'numeroSerie':'" + numeroSerie + '\'' +
                ", 'capacidade':'" + capacidade + '\'' +
                ", 'usado':'" + usado + '\'' +
                ", 'disponivel':'" + disponivel + '\'' +
                "}";
    }
}
