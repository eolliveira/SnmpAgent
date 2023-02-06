package com.example.SnmpAgent;

import com.example.SnmpAgent.services.SnmpAgentReceiver;

import java.io.IOException;

public class SnmpAgentApplication {
    private static String COMMUNITY = "public";
    private static final String IPADDRESS = "127.0.0.1";
    private static String OID = ".1.3.6.1.2.1.1.8";
    private static String portTrap = "162";

    public static void main(String[] args) throws IOException {
//        //lança trap para o manager
//        SnmpTrapSender trapSender = new SnmpTrapSender();
//        trapSender.sendTrapV1(COMMUNITY, IPADDRESS, portTrap, OID);


        try {
            // crie um agente receptor em localhost:161
            SnmpAgentReceiver agentReceiver = new SnmpAgentReceiver("0.0.0.0/161");

            // realmente comece a ouvir
            agentReceiver.start();

            // register the custom mib information
            agentReceiver.registerCustomMIB();

            System.out.println("AgentSNMP ouvindo na porta 161...");

            // apenas continue executando o processo
            // em um cenário normal, o agente será instanciado em um processo vivo
            while(true) {
                Thread.sleep(10000);
            }

        } catch (Exception e) {
            System.out.println("Falha ao iniciar o agente SNMP na porta 161 : " + e.getMessage());
        }

    }


}
