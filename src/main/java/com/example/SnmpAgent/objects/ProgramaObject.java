package com.example.SnmpAgent.objects;

public class ProgramaObject {

    private String nome;
    private String dtInstalacao;

    public ProgramaObject(String nome, String dtInstalacao) {
        this.nome = nome;
        this.dtInstalacao = dtInstalacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDtInstalacao() {
        return dtInstalacao;
    }

    public void setDtInstalacao(String dtInstalacao) {
        this.dtInstalacao = dtInstalacao;
    }

    @Override
    public String toString() {
        return "{" +
                "'nome':'" + nome + '\'' +
                ", 'dtInstalacao':'" + dtInstalacao + '\'' +
                "}";
    }

}
