package com.example.SnmpAgent;

import com.example.SnmpAgent.services.SnmpTrapSender;

import java.io.IOException;

public class SnmpAgentApplication {
    private static String COMMUNITY = "public";
    private static final String IPADDRESS = "127.0.0.1";
    private static String OID = ".1.3.6.1.2.1.1.8";
    private static String PORT = "162";

    public static void main(String[] args) throws IOException {

        //lança trap para o manager
        SnmpTrapSender trapSender = new SnmpTrapSender();
        trapSender.sendTrapV1(COMMUNITY, IPADDRESS, PORT, OID);
    }


}
