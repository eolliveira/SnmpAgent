package com.example.SnmpAgent.objects;

public class PlacaMaeObject {

    private String modelo;
    private String serialNumber;

    public PlacaMaeObject(String nome, String serialNumber) {
        this.modelo = nome;
        this.serialNumber = serialNumber;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "{" +
                "'modelo':'" + modelo + '\'' +
                ", 'numeroSerie':'" + serialNumber + '\'' +
                "}";
    }

}
