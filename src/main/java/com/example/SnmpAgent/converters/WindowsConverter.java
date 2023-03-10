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
import java.util.Objects;


public class WindowsConverter {

    private final static String SCRIPT_INSTALLED_PROGRAMS = "cmd /c cd C:\\Windows\\System32\\wbem && wmic product get name,installDate";
    private WindowsObject windows = new WindowsObject();
    private SystemInfo si = new SystemInfo();
    private HardwareAbstractionLayer hal = si.getHardware();
    private OperatingSystem os = si.getOperatingSystem();

    public WindowsObject getConvertedData() throws IOException, InterruptedException {

        windows.setSistemaOperacional(os.toString());
        windows.setArquiteturaSo(os.getBitness());
        windows.setFabricante(Objects.equals(hal.getComputerSystem().getManufacturer(), "System manufacturer") ? getPlacaMaeDesktop().get(0).getFabricante() : hal.getComputerSystem().getManufacturer());
        windows.setModelo(Objects.equals(hal.getComputerSystem().getModel(), "System Product Name") ? getPlacaMaeDesktop().get(0).getModelo() : hal.getComputerSystem().getModel());
        windows.setNumeroSerie(Objects.equals(hal.getComputerSystem().getSerialNumber(), "System Serial Number") ? getPlacaMaeDesktop().get(0).getSerialNumber() : hal.getComputerSystem().getSerialNumber());
        windows.setNomeHost(os.getNetworkParams().getHostName());
        windows.setDominio(os.getNetworkParams().getDomainName());
        windows.setGateway(os.getNetworkParams().getIpv4DefaultGateway());
        windows.setUltimoUsuarioLogado(System.getProperty("user.name"));
        windows.setTempoLigado(FormatUtil.formatElapsedSecs(os.getSystemUptime()).replace("days", "dias"));
        windows.setProcessador(getProcesssor());
        windows.setMemoriaRam(getRamMemory());
        windows.setDnsList(getDnsList());
        windows.setIntefaces(getListaInterfaces());
        windows.setDiscos(getListaDiscos());
        windows.setImpressoras(getImpressorasInstaladas());
        windows.setPlascasVideo(getPlacasVideo());
        windows.setProgramasIntalados(getProgramasInstalados());

        return windows;
    }

    private static List<PlacaMaeObject> getPlacaMaeDesktop() throws IOException, InterruptedException {
        //TODO(TESTAR MODELO PLACA MÃE)
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd C:\\Windows\\System32\\wbem && wmic baseboard get product,Manufacturer,serialnumber");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<PlacaMaeObject> placaMaeList = new ArrayList<>();

        String line2;
        while ((line2 = reader.readLine()) != null) {
            if (!line2.isEmpty()) {
                String strFab = line2.substring(0, line2.indexOf("  "));
                String strModel = line2.substring(line2.indexOf("  ")).trim();
                String fabricante = strFab;
                String modelo = strModel.substring(0, strModel.indexOf("  "));
                String serialNumber =  strModel.trim().substring(strModel.trim().indexOf("  ")).trim();
                PlacaMaeObject pm = new PlacaMaeObject(fabricante, modelo, serialNumber);
                placaMaeList.add(pm);
            }
        }

        process.waitFor();
        placaMaeList.remove(0);
        return placaMaeList;
    }

    private List<PlacaVideoObject> getPlacasVideo() {
        List<PlacaVideoObject> placas = new ArrayList<>();
        for (GraphicsCard placa : hal.getGraphicsCards()) {
            String versao = placa.getVersionInfo().substring(placa.getVersionInfo().indexOf("=") + 1);
            PlacaVideoObject obj = new PlacaVideoObject(
                    placa.getName().replaceAll("[^\\x00-\\x7F]+", "@"),
                    placa.getVendor().replaceAll("[^\\x00-\\x7F]+", "@"), versao);
            placas.add(obj);
        }
        return placas;
    }

    private static List<ProgramaObject> getProgramasInstalados() throws IOException {
        Process p = Runtime.getRuntime().exec(SCRIPT_INSTALLED_PROGRAMS);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        List<ProgramaObject> programas = new ArrayList<>();

        String line;
        while ((line = stdInput.readLine()) != null) {
            if (!line.isEmpty()) {
                String dtIntalacao = line.substring(0, line.indexOf("   "));
                String software = line.substring(line.indexOf("   "));
                ProgramaObject programa = new ProgramaObject(software.trim().replaceAll("[^\\x00-\\x7F]+", "@"), dtIntalacao.trim());
                programas.add(programa);
            }
        }
        p.destroy();

        if (!programas.isEmpty()) {
            programas.remove(0);
        }
        return programas;
    }

    private static List<ImpressoraObject> getImpressorasInstaladas() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        List<ImpressoraObject> printers = new ArrayList<>();
        for (PrintService printer : printServices)
            printers.add(new ImpressoraObject(printer.toString().substring(16)));
        return printers;
    }

    private List<DiscoRigidoObject> getListaDiscos() {
        List<DiscoRigidoObject> listaDiscos = new ArrayList<>();
        for (HWDiskStore disc : hal.getDiskStores()) {
            DiscoRigidoObject obj2 = new DiscoRigidoObject(
                    disc.getName().substring(4),
                    disc.getModel().substring(0, disc.getModel().indexOf("(") - 1),
                    disc.getSerial().trim(),
                    disc.getSize());
            for (HWPartition partition : disc.getPartitions()) {
                ParticaoObject part = new ParticaoObject(
                        partition.getMountPoint(),
                        partition.getSize(),
                        new File(partition.getMountPoint() + "/").getTotalSpace() - new File(partition.getMountPoint() + "/").getFreeSpace());
                obj2.getParticoes().add(part);
            }

            listaDiscos.add(obj2);
        }
        return listaDiscos;
    }

    private List<InterfaceRedeObject> getListaInterfaces() {
        List<InterfaceRedeObject> listInterface = new ArrayList<>();
        for (NetworkIF dns : hal.getNetworkIFs()) {
            InterfaceRedeObject obj = new InterfaceRedeObject(
                    dns.getName().trim(),
                    dns.getDisplayName().trim(),
                    dns.getMacaddr().toUpperCase().trim(),
                    dns.getIPv4addr(),
                    dns.getSubnetMasks());
            listInterface.add(obj);
        }
        return listInterface;
    }

    private List<String> getDnsList() {
        String[] dnsServers = os.getNetworkParams().getDnsServers();
        List<String> dnsList = new ArrayList<>();
        for (String dns : dnsServers) {
            dnsList.add(dns);
        }
        return dnsList;
    }

    private String getProcesssor() {
        String x = hal.getProcessor().toString();
        int index = x.indexOf("\n");
        String processsor = x.substring(0, index);
        return processsor;
    }

    private String getRamMemory() {
        String y = hal.getMemory().toString();
        int init = y.indexOf("/");
        String ramMemory = y.substring(init + 1, y.length() - 4);
        return ramMemory;
    }
}
