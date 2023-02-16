package com.example.SnmpAgent.converters;

import com.example.SnmpAgent.objects.*;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class WindowsConverter {
    WindowsObject windows = new WindowsObject();
    SystemInfo si = new SystemInfo();
    HardwareAbstractionLayer hal = si.getHardware();
    OperatingSystem os = si.getOperatingSystem();

    public WindowsObject getConvertedData() throws IOException {

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
                    disc.getModel().substring(0, disc.getModel().indexOf("(") - 1),
                    disc.getSerial(),
                    disc.getSize());
            for(HWPartition partition : disc.getPartitions()) {
                ParticaoObject part = new ParticaoObject(
                        partition.getMountPoint(),
                        partition.getSize(),
                        new File(partition.getMountPoint() + "/").getTotalSpace() - new File(partition.getMountPoint() + "/").getFreeSpace());
                obj2.getParticoes().add(part);
            }

            listaDiscos.add(obj2);
        }


        //lista impressoras instaladas
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        List<ImpressoraObject> printers = new ArrayList<>();
        for (PrintService printer : printServices)
            printers.add(new ImpressoraObject(printer.toString().substring(16)));


        //plascas de video
        List<PlacaVideoObject> placas = new ArrayList<>();
        for(GraphicsCard placa: hal.getGraphicsCards()){
            PlacaVideoObject obj =  new PlacaVideoObject(placa.getName(), placa.getVendor(), placa.getVersionInfo());
            placas.add(obj);
        }


        //Programas instalados

        Process p = Runtime.getRuntime().exec("cmd /c wmic product get name,installDate");
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        List<ProgramaObject> programas  = new ArrayList<>();

        String line;
        //printa o retorno
        while ((line = stdInput.readLine()) != null) {
            if(!line.isEmpty()) {
                String dtIntalacao = line.substring(0, line.indexOf("   "));
                String software = line.substring(line.indexOf("   "));
                ProgramaObject programa = new ProgramaObject(software.trim(), dtIntalacao.trim());
                programas.add(programa);
            }
        }

        p.destroy();
        programas.remove(0);



        windows.setSistemaOperacional(os.toString());
        windows.setArquiteturaSo(os.getBitness());
        windows.setFabricante(hal.getComputerSystem().getManufacturer());
        windows.setModelo(hal.getComputerSystem().getModel());
        windows.setNumeroSerie(hal.getComputerSystem().getSerialNumber());
        windows.setNomeHost(os.getNetworkParams().getHostName());
        windows.setDominio(os.getNetworkParams().getDomainName());
        windows.setGateway(os.getNetworkParams().getIpv4DefaultGateway());
        windows.setUltimoUsuarioLogado(System.getProperty("user.name"));
        windows.setTempoLigado(FormatUtil.formatElapsedSecs(os.getSystemUptime()));
        windows.setProcessador(processsor);
        windows.setMemoriaRam(ramMemory);
        windows.setDnsList(dnsList);
        windows.setIntefaces(listInterface);
        windows.setDiscos(listaDiscos);
        windows.setImpressoras(printers);
        windows.setPlascasVideo(placas);
        windows.setProgramasIntalados(programas);

        return windows;
    }
}
