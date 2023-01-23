package com.example.SnmpAgent.services;

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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SnmpAgentReceiver extends BaseAgent {


    ///////////////////
    private String address;

    // Example: new SnmpAgentReceiver("0.0.0.0/161");
    public SnmpAgentReceiver(String address) throws IOException {


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

    /**
     * Adiciona a comunidade aos mapeamentos de nomes de segurança necessários para SNMPv1 e SNMPv2c.
     */
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

    /**
     * Adds initial notification targets and filters.
     */
    @Override
    protected void addNotificationTargets(SnmpTargetMIB arg0, SnmpNotificationMIB arg1) {
    }

    /**
     * Adds all the necessary initial users to the USM.
     */
    @Override
    protected void addUsmUser(USM arg0) {
    }

    /**
     * Adds initial VACM configuration.
     */
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

    /**
     * Unregister the basic MIB modules from the agent's MOServer.
     */
    @Override
    protected void unregisterManagedObjects() {
    }

    /**
     * Register additional managed objects at the agent's server.
     */
    @Override
    protected void registerManagedObjects() {
    }

    protected void initTransportMappings() throws IOException {

        transportMappings = new TransportMapping[1];
        transportMappings[0] = TransportMappings.getInstance().createTransportMapping(GenericAddress.parse(address));
    }

    /**
     * Start method invokes some initialization methods needed to start the agent
     */
    public void start() throws IOException {
        init();
        // loadConfig(ImportModes.REPLACE_CREATE);  // This method reads some old config from a file and causes unexpected behavior.
        addShutdownHook();
        getServer().addContext(new OctetString("public"));
        finishInit();
        run();
        sendColdStartNotification();
    }

    /**
     * Clients can register the MO they need
     */
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

    public void registerCustomMIB() throws UnknownHostException, UnsupportedEncodingException {

        // no need to answer for default OIDs loaded by the base class
        unregisterManagedObject(getSnmpv2MIB());

        /*
            custom 1.3.6.1.4.1.12345
            iso(1).org(3).dod(6).internet(1).private(4).enterprises(1).12345

            .1 general
                .1 generalGreeting OCTET STRING
                .2 generalRandom Integer32
            .2 date
                .1 dateString OCTET STRING
                .2 dateParts
                    .1 datePartsDay Integer32
                    .2 datePartsMonth Integer32
                    .3 datePartsYear Integer32
         */

        // this is the OID
        String customMibOid = ".1.3.6.1.4.1.12345";

        // register all custom MIB data

        // general

        System.out.println();

        InetAddress addr = InetAddress.getLocalHost();
        byte[] macAddress = addr.getAddress();
        String hostname = addr.getHostName();


        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.3.0", hostname));
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".1.1.0", System.getProperty("user.dir") ));
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".1.2.0", System.getProperty("sun.arch.data.model")));

        // license

        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.1.0", System.getProperty("os.name")));
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.1.0", System.getProperty("user.name")));
        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.2.0", new String(macAddress)));


        ////////////////

//        // this is the OID
//        String customMibOid = ".1.3.6.1.4.1.12345";
//
//        // register all custom MIB data
//
//        // general
//
//        System.out.println();
//
//        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".1.1.0", "generalGreeting"));
//        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".1.2.0", "generalRandom"));
//
//        // license
//
//        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.1.0", System.getProperty("os.name")));
//        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.1.0", "datePartsDay"));
//        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.2.0", "datePartsMonth"));
//        registerManagedObject(ManagedObjectFactory.createReadOnly(customMibOid + ".2.2.3.0", "datePartsYear"));
    }
}
