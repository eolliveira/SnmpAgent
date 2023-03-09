package com.example.SnmpAgent.objects;

public class PlacaMaeObject {

    private String fabricante;
    private String modelo;
    private String serialNumber;

    public PlacaMaeObject(String modelo, String serialNumber) {
        this.modelo = modelo;
        this.serialNumber = serialNumber;
    }

    public PlacaMaeObject(String fabricante, String modelo, String serialNumber) {
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.serialNumber = serialNumber;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
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
                ", 'fabricante':'" + fabricante + '\'' +
                ", 'numeroSerie':'" + serialNumber + '\'' +
                "}";
    }

}
