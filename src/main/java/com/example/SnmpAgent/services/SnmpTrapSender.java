package com.example.SnmpAgent.services;

import org.snmp4j.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

public class SnmpTrapSender {

    public void sendTrapV1(String community, String ipaddress, String port, String oid) throws IOException {
        TransportMapping transportMapping = new DefaultUdpTransportMapping();
        transportMapping.listen();

        CommunityTarget communityTarget = new CommunityTarget();
        communityTarget.setCommunity(new OctetString(community));
        communityTarget.setVersion(SnmpConstants.version2c);
        communityTarget.setAddress(new UdpAddress(ipaddress + "/" + port));
        communityTarget.setTimeout(5000);
        communityTarget.setRetries(2);

//        PDUv1 pdUv1 = new PDUv1();
//        pdUv1.add(new VariableBinding(SnmpConstants.sysLocation, new OctetString(new Date().toString())));
//        pdUv1.setType(PDU.V1TRAP);
//        pdUv1.setEnterprise(new OID(oid));
//        pdUv1.setGenericTrap(PDUv1.ENTERPRISE_SPECIFIC);
//        pdUv1.setSpecificTrap(1);
//        //pdUv1.setAgentAddress(new IpAddress(ipaddress));
//        pdUv1.setAgentAddress(new IpAddress(InetAddress.getLocalHost().getHostAddress()));


        // Create PDU for V2
        PDU pdu = new PDU();
        // need to specify the system up time
        pdu.add(new VariableBinding(SnmpConstants.sysUpTime, new OctetString(new Date().toString())));
        pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(oid)));
        pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(InetAddress.getLocalHost().getHostAddress())));
        pdu.add(new VariableBinding(new OID(oid), new OctetString("Major")));
        pdu.setType(PDU.NOTIFICATION);

        Snmp snmp = new Snmp(transportMapping);
        System.out.println("Sending v1 trap ---> ");
        //System.out.println(InetAddress.getLocalHost().getHostAddress());
        snmp.send(pdu, communityTarget);
        snmp.close();
    }


}
