package com.example.SnmpAgent.converters;

import com.example.SnmpAgent.objects.DiscoRigidoObject;
import com.example.SnmpAgent.objects.InterfaceRedeObject;
import com.example.SnmpAgent.objects.WindowsObject;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.util.ArrayList;
import java.util.List;

public class WindowsConverter {
    WindowsObject windows = new WindowsObject();
    SystemInfo si = new SystemInfo();
    HardwareAbstractionLayer hal = si.getHardware();
    OperatingSystem os = si.getOperatingSystem();

    public WindowsObject getConvertedData() {

        //recorte processador
        String x = hal.getProcessor().toString();
        int index = x.indexOf("\n");
        String processsor = x.substring(0, index);

        //recorte memory
        String y = hal.getMemory().toString();
        int init = y.indexOf("/");
        String ramMemory = y.substring(init + 1, y.length() - 4);


        //lista de dns
        String[] dnsServers = os.getNetworkParams().getDnsServers();
        List<String> dnsList = new ArrayList<>();
        for (String dns : dnsServers) {
            dnsList.add(dns);
        }

        //lista de interfaces
        List<InterfaceRedeObject> listInterface = new ArrayList<>();
        for (NetworkIF dns : hal.getNetworkIFs()) {
            InterfaceRedeObject obj = new InterfaceRedeObject(
                    dns.getName(),
                    dns.getDisplayName(),
                    dns.getMacaddr(),
                    dns.getIPv4addr(),
                    dns.getSubnetMasks());
            listInterface.add(obj);
        }

        //lista de discos
        List<DiscoRigidoObject> listaDiscos = new ArrayList<>();
        for (HWDiskStore disc : hal.getDiskStores()) {
            DiscoRigidoObject obj2 = new DiscoRigidoObject(
                    disc.getName().substring(4),
                    "disc.getModel()",
                    disc.getSerial(),
                    disc.getSize(),
                    disc.getReads(),
                    disc.getWrites());
            listaDiscos.add(obj2);
        }

        windows.setOs(os.toString());
        windows.setOsArchitecture(os.getBitness());
        windows.setManufacturer(hal.getComputerSystem().getManufacturer());
        windows.setModel(hal.getComputerSystem().getModel());
        windows.setSerialNumber(hal.getComputerSystem().getSerialNumber());
        windows.setHostname(os.getNetworkParams().getHostName());
        windows.setDomain(os.getNetworkParams().getDomainName());
        windows.setGateway(os.getNetworkParams().getIpv4DefaultGateway());
        windows.setLastUserloggedIn(System.getProperty("user.name"));
        windows.setProcessor(processsor);
        windows.setRamMemory(ramMemory);
        windows.setDnsList(dnsList);
        windows.setIntefaces(listInterface);
        windows.setDisks(listaDiscos);

        return windows;
    }
}
