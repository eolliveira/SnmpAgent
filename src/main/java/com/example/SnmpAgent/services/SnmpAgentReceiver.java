package com.example.SnmpAgent.services;

import com.example.SnmpAgent.converters.WindowsConverter;
import com.example.SnmpAgent.mibs.WindowsMIB;
import com.example.SnmpAgent.objects.WindowsObject;
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
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

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

    protected void initTransportMappings() {
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

    public void registerCustomMIB() throws IOException {

        unregisterManagedObject(getSnmpv2MIB());

        System.out.println();

        WindowsMIB mib = new WindowsMIB();
        WindowsConverter converter = new WindowsConverter();
        WindowsObject win = converter.getConvertedData();

        // register all custom MIB data
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.SO_OID, win.getSistemaOperacional()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.ARQUITETURA_SO_OID, win.getArquiteturaSo()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.FABRICANTE_OID, win.getFabricante()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.MODELO_OID, win.getModelo()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.NUMERO_SERIE_OID, win.getNumeroSerie()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.PROCESSADOR_OID, win.getProcessador()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.MEMORIA_RAM_OID, win.getMemoriaRam()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.NOME_OID, win.getNomeHost()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.DOMINIO_OID, win.getDominio()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.GATEWAY_OID, win.getGateway()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.DNS_OID, win.getDnsList()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.USUARIO_LOGADO_OID, win.getUltimoUsuarioLogado()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.TEMPO_LIGADO_OID, win.getTempoLigado()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.INTERFACES_OID, win.getIntefaces()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.DISCO_RIGIDO_OID, win.getDiscos()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.IMPRESSORAS_OID, win.getImpressoras()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.PLACAS_VIDEO_OID, win.getPlascasVideo()));
        registerManagedObject(ManagedObjectFactory.createReadOnly(mib.PROGRAMAS_OID, win.getProgramasIntalados()));


        //teste
        WindowsObject windows = new WindowsObject();
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();


    }

}

