package com.example.SnmpAgent.objects;

import java.util.ArrayList;
import java.util.List;

public class WindowsObject {

    private String os;
    private String architectureSo;
    private String manufacturer;
    private String model;
    private String serialNumber;
    private String processor;
    private String ramMemory;
    private String hostname;
    private String domain;
    private String ipAdrress;
    private String macAddress;
    private String gateway;
    private String primaryDns;
    private String secondaryDns;
    private List<InterfaceObject> intefaces = new ArrayList<>();

    public WindowsObject() {}

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getArchitectureSo() {
        return architectureSo;
    }

    public void setArchitectureSo(String architectureSo) {
        this.architectureSo = architectureSo;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getRamMemory() {
        return ramMemory;
    }

    public void setRamMemory(String ramMemory) {
        this.ramMemory = ramMemory;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIpAdrress() {
        return ipAdrress;
    }

    public void setIpAdrress(String ipAdrress) {
        this.ipAdrress = ipAdrress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getPrimaryDns() {
        return primaryDns;
    }

    public void setPrimaryDns(String primaryDns) {
        this.primaryDns = primaryDns;
    }

    public String getSecondaryDns() {
        return secondaryDns;
    }

    public void setSecondaryDns(String secondaryDns) {
        this.secondaryDns = secondaryDns;
    }

    public List<InterfaceObject> getIntefaces() {
        return intefaces;
    }
}
