package com.example.SnmpAgent.services;

import org.snmp4j.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

public class SnmpTrapSender {

    public void sendTrapV1(String community, String ipaddress, String port, String oid) throws IOException {
        TransportMapping transportMapping = new DefaultUdpTransportMapping();
        transportMapping.listen();

        CommunityTarget communityTarget = new CommunityTarget();
        communityTarget.setCommunity(new OctetString(community));
        communityTarget.setVersion(SnmpConstants.version1);
        communityTarget.setAddress(new UdpAddress(ipaddress+"/"+port));
        communityTarget.setTimeout(5000);
        communityTarget.setRetries(2);

        PDUv1 pdUv1 = new PDUv1();
        pdUv1.setType(PDU.V1TRAP);
        pdUv1.setEnterprise(new OID(oid));
        pdUv1.setGenericTrap(PDUv1.ENTERPRISE_SPECIFIC);
        pdUv1.setSpecificTrap(1);
        pdUv1.setAgentAddress(new IpAddress(ipaddress));


        Snmp snmp = new Snmp(transportMapping);
        System.out.println("Sending v1 trap ---> ");
        snmp.send(pdUv1, communityTarget);
        snmp.close();
    }


}
