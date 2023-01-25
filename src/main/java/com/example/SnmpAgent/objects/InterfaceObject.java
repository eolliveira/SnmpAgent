package com.example.SnmpAgent.objects;

import java.util.Arrays;

public class InterfaceObject {

    private String nomeLocal;
    private String nomeFabricante;
    private String enderecoMac;
    private String enderecoIp;
    private String mascaraSubRede;

    public InterfaceObject(){}

    public InterfaceObject(String name, String displayName, String macaddr, String[] iPv4addr, Short[] subnetMasks) {
        this.nomeLocal = name;
        this.nomeFabricante = displayName;
        this.enderecoMac = macaddr;

        //converte ip
        String ipAddress = "";
        for (String s: iPv4addr) {
            ipAddress = ipAddress + s;
        }

        //converte mascara de rede
        String mask = "";
        for (Short m: subnetMasks) {
            mask = mask + m.shortValue();
        }

        this.enderecoIp = ipAddress;
        this.mascaraSubRede = mask;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public String getNomeFabricante() {
        return nomeFabricante;
    }

    public void setNomeFabricante(String nomeFabricante) {
        this.nomeFabricante = nomeFabricante;
    }

    public String getEnderecoMac() {
        return enderecoMac;
    }

    public void setEnderecoMac(String enderecoMac) {
        this.enderecoMac = enderecoMac;
    }

    public String getEnderecoIp() {
        return enderecoIp;
    }

    public void setEnderecoIp(String enderecoIp) {
        this.enderecoIp = enderecoIp;
    }

    public String getMascaraSubRede() {
        return mascaraSubRede;
    }

    public void setMascaraSubRede(String mascaraSubRede) {
        this.mascaraSubRede = mascaraSubRede;
    }

    @Override
    public String toString() {
        return "{" +
                "'nomeLocal':'" + nomeLocal + '\'' +
                ", 'nomeFabricante':'" + nomeFabricante + '\'' +
                ", 'enderecoMac':'" + enderecoMac + '\'' +
                ", 'enderecoIp':'" + enderecoIp + '\'' +
                ", 'mascaraSubRede':'" + mascaraSubRede + '\'' +
                '}';
    }
}
