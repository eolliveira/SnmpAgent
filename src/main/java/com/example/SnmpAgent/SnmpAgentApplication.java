package com.example.SnmpAgent;

import com.example.SnmpAgent.services.SnmpAgentReceiver;
import com.example.SnmpAgent.services.SnmpTrapSender;

import java.io.IOException;

public class SnmpAgentApplication {
    private static String COMMUNITY = "public";
    private static String IPADDRESS = "10.0.5.36";
    private static String OID = ".1.3.6.1.2.1.1.8";
    private static String PORTTRAP = "1062";

    public static void main(String[] args) throws IOException {
        //lança trap para o manager
        System.out.println("Iniciando SnmpTrapSender na porta " + PORTTRAP);
        SnmpTrapSender trapSender = new SnmpTrapSender();
        trapSender.sendTrapV1(COMMUNITY, IPADDRESS, PORTTRAP, OID);

        try {
            // crie um agente receptor em localhost:161
            SnmpAgentReceiver agentReceiver = new SnmpAgentReceiver("0.0.0.0/1061");

            // realmente comece a ouvir
            agentReceiver.start();

            // register the custom mib information
            agentReceiver.registerCustomMIB();

            System.out.println("AgentSNMP ouvindo na porta 1061...");

            // apenas continue executando o processo
            while(true) {
                Thread.sleep(10000);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Falha ao iniciar o agente SNMP na porta 1061 : " + e.getMessage());
        }

    }


}
