package com.example.SnmpAgent.objects;

import java.util.ArrayList;
import java.util.List;

public class WindowsObject {

    private String sistemaOperacional;
    private Integer arquiteturaSo;
    private String fabricante;
    private String modelo;
    private String numeroSerie;
    private String processador;
    private String memoriaRam;
    private String nomeHost;
    private String dominio;
    private String gateway;
    private String ultimoUsuarioLogado;
    private List<String> dnsList;
    private List<InterfaceRedeObject> intefaces = new ArrayList<>();

    private List<DiscoRigidoObject> discos = new ArrayList<>();

    private List<ImpressoraObject> impressoras = new ArrayList<>();

    public WindowsObject() {
    }

    public List<String> getDnsList() {
        return dnsList;
    }

    public List<ImpressoraObject> getImpressoras() {
        return impressoras;
    }

    public void setImpressoras(List<ImpressoraObject> impressoras) {
        this.impressoras = impressoras;
    }

    public void setDnsList(List<String> dnsList) {
        this.dnsList = dnsList;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public void setSistemaOperacional(String sistemaOperacional) {
        this.sistemaOperacional = sistemaOperacional;
    }

    public Integer getArquiteturaSo() {
        return arquiteturaSo;
    }

    public void setArquiteturaSo(Integer arquiteturaSo) {
        this.arquiteturaSo = arquiteturaSo;
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

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getProcessador() {
        return processador;
    }

    public void setProcessador(String processador) {
        this.processador = processador;
    }

    public String getMemoriaRam() {
        return memoriaRam;
    }

    public void setMemoriaRam(String memoriaRam) {
        this.memoriaRam = memoriaRam;
    }

    public String getNomeHost() {
        return nomeHost;
    }

    public void setNomeHost(String nomeHost) {
        this.nomeHost = nomeHost;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getUltimoUsuarioLogado() {
        return ultimoUsuarioLogado;
    }

    public void setUltimoUsuarioLogado(String ultimoUsuarioLogado) {
        this.ultimoUsuarioLogado = ultimoUsuarioLogado;
    }

    public List<InterfaceRedeObject> getIntefaces() {
        return intefaces;
    }

    public void setIntefaces(List<InterfaceRedeObject> intefaces) {
        this.intefaces = intefaces;
    }

    public List<DiscoRigidoObject> getDiscos() {
        return discos;
    }

    public void setDiscos(List<DiscoRigidoObject> discos) {
        this.discos = discos;
    }
}
