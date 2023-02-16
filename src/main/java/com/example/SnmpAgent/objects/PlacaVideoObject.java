package com.example.SnmpAgent.objects;

public class PlacaVideoObject {

    private String nome;
    private String fabricante;
    private String versaoDrive;

    public PlacaVideoObject(String nome, String fabricante, String versaoDrive) {
        this.nome = nome;
        this.fabricante = fabricante;
        this.versaoDrive = versaoDrive;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getVersaoDrive() {
        return versaoDrive;
    }

    public void setVersaoDrive(String versaoDrive) {
        this.versaoDrive = versaoDrive;
    }

    @Override
    public String toString() {
        return "{" +
                "'nome':'" + nome + '\'' +
                ", 'fabricante':'" + fabricante + '\'' +
                ", 'versaoDrive':'" + versaoDrive + '\'' +
                "}";
    }
}
