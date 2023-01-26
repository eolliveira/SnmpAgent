package com.example.SnmpAgent.services;

import com.example.SnmpAgent.objects.DiscoRigidoObject;
import com.example.SnmpAgent.objects.InterfaceRedeObject;
import org.snmp4j.TransportMapping;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.*;
import org.snmp4j.transport.TransportMappings;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SnmpAgentReceiver extends BaseAgent {
    private String address;

    // Example: new SnmpAgentReceiver("0.0.0.0/161");
    public SnmpAgentReceiver(String address) {

        /*
         * Cria um agente base com contador de inicialização, arquivo de configuração e um
         * CommandProcessor para processamento de solicitações SNMP. Parâmetros:
         * "bootCounterFile" - um arquivo com informações serializadas do contador de inicialização
         * (ler escrever). Se o arquivo não existir, ele será criado no desligamento do
         * o agente. "configFile" - um arquivo com configuração serializada
         * informações (ler/escrever). Se o arquivo não existir, ele é criado em
         * encerramento do agente. "commandProcessor" - o CommandProcessor
         * instância que lida com as solicitações SNMP.
         * */
        super(
                new File("conf.agent"),
                new File("bootCounter.agent"),
                new CommandProcessor(new OctetString(MPv3.createLocalEngineID()))
        );
        this.address = address;
    }


    //Adiciona a comunidade aos mapeamentos de nomes de segurança necessários para SNMPv1 e SNMPv2c.
    @Override
    protected void addCommunities(SnmpCommunityMIB communityMIB) {

        Variable[] com2sec = new Variable[]{
                new OctetString("public"),
                new OctetString("notConfigUser"), // security name
                getAgent().getContextEngineID(), // local engine ID
                new OctetString(), // default context name
                new OctetString(), // transport tag
                new Integer32(StorageType.nonVolatile), // storage type
                new Integer32(RowStatus.active) // row status
        };

        SnmpCommunityMIB.SnmpCommunityEntryRow row =
                communityMIB.getSnmpCommunityEntry().createRow(new OctetString("notConfigUser").toSubIndex(true), com2sec);

        communityMIB.getSnmpCommunityEntry().addRow(row);
    }


    //Adiciona alvos e filtros de notificação iniciais.
    @Override
    protected void addNotificationTargets(SnmpTargetMIB arg0, SnmpNotificationMIB arg1) {
    }

    /**
     * Adds all the necessary initial users to the USM.
     */
    @Override
    protected void addUsmUser(USM arg0) {
    }


    // Adiciona a configuração inicial do VACM.
    @Override
    protected void addViews(VacmMIB vacm) {

        vacm.addGroup(
                SecurityModel.SECURITY_MODEL_SNMPv1,
                new OctetString("notConfigUser"),
                new OctetString("notConfigGroup"),
                StorageType.nonVolatile
        );

        vacm.addGroup(
                SecurityModel.SECURITY_MODEL_SNMPv2c,
                new OctetString("notConfigUser"),
                new OctetString("notConfigGroup"),
                StorageType.nonVolatile
        );

        vacm.addAccess(
                new OctetString("notConfigGroup"),
                new OctetString(),
                SecurityModel.SECURITY_MODEL_ANY,
                SecurityLevel.NOAUTH_NOPRIV,
                MutableVACM.VACM_MATCH_EXACT,
                new OctetString("systemview"),
                new OctetString(),
                new OctetString(),
                StorageType.nonVolatile
        );

        vacm.addViewTreeFamily(
                new OctetString("systemview"),
                new OID("1.3.6.1.4.1.12345"),
                new OctetString(),
                VacmMIB.vacmViewIncluded,
                StorageType.nonVolatile
        );
    }


    @Override
    protected void unregisterManagedObjects() {
    }


    @Override
    protected void registerManagedObjects() {
    }

    protected void initTransportMappings() throws IOException {

        transportMappings = new TransportMapping[1];
        transportMappings[0] = TransportMappings.getInstance().createTransportMapping(GenericAddress.parse(address));
    }

    public void start() throws IOException {
        init();
        addShutdownHook();
        getServer().addContext(new OctetString("public"));
        finishInit();
        run();
        sendColdStartNotification();
    }


    public void registerManagedObject(ManagedObject mo) {
        try {
            server.register(mo, null);
        } catch (DuplicateRegistrationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void unregisterManagedObject(MOGroup moGroup) {
        moGroup.unregisterMOs(server, getContext(moGroup));
    }

    public void registerCustomMIB() {

        unregisterManagedObject(getSnmpv2MIB());

        // this is the OID
        String customMibOid = ".1.3.6.1.4.1.12345";

        // register all custom MIB data

        System.out.println();

        SystemInfo si = new SystemInfo();

        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();

        String x = hal.getProcessor().toString();
        int index = x.indexOf("\n");
        String processsor = x.substring(0, index);

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
            InterfaceRedeObject obj = new InterfaceRedeObject(dns.getName(), dns.getDisplayName(), dns.getMacaddr(), dns.getIPv4addr(), dns.getSubnetMasks());
            listInterface.add(obj);
        }

        //lista de discos
        List<DiscoRigidoObject> listaDiscos = new ArrayList<>();
        for (HWDiskStore disc : hal.getDiskStores()) {
            DiscoRigidoObject obj2 = new DiscoRigidoObject(disc.getName().substring(4), disc.getModel(), disc.getSerial(), disc.getSize(), disc.getReads(), disc.getWrites());
            listaDiscos.add(obj2);
        }

        System.out.println(hal.getDiskStores().get(1).getSize());

        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".1.1.0", os.toString()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".1.2.0", os.getBitness() + "bits"));
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".1.3.0", hal.getComputerSystem().getManufacturer()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".1.4.0", hal.getComputerSystem().getModel()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".1.5.0", hal.getComputerSystem().getSerialNumber()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".1.6.0", processsor));
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.1.0", ramMemory + " Gb")); //memoria
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.1.0", os.getNetworkParams().getHostName())); //hostname
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.2.0", os.getNetworkParams().getDomainName())); //dominio
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.3.0", os.getNetworkParams().getIpv4DefaultGateway())); //gateway
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.4.0", dnsList)); //lista servidore
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.5.0", System.getProperty("user.name"))); //usuario logado
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.6.0", listInterface)); //listaa de interfaces
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.7.0", listaDiscos)); //lista de discos
    }


}

