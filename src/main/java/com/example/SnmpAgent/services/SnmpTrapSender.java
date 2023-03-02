package com.example.SnmpAgent.services;

import com.example.SnmpAgent.converters.WindowsConverter;
import com.example.SnmpAgent.mibs.WindowsMIB;
import com.example.SnmpAgent.objects.WindowsObject;
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

        // Create PDU for V2
        PDU pdu = new PDU();

        pdu.add(new VariableBinding(SnmpConstants.sysDescr, new OctetString( "SYNC_REQUEST")));
        pdu.add(new VariableBinding(SnmpConstants.sysName, new OctetString( "WORKSTATION")));
        pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(InetAddress.getLocalHost().getHostAddress())));
        pdu.add(new VariableBinding(SnmpConstants.sysUpTime, new OctetString(new Date().toString())));


        ///////////////////teste para enviar informações do ativo ja no trap
        WindowsMIB mib = new WindowsMIB();
        WindowsConverter converter = new WindowsConverter();
        WindowsObject win = converter.getConvertedData();

        pdu.add(new VariableBinding(new OID(mib.PROCESSADOR_OID), new OctetString( win.getProcessador())));

        ////////////////////////////////////////



        pdu.setType(PDU.NOTIFICATION);

        Snmp snmp = new Snmp(transportMapping);
        System.out.println("Sending v1 trap ---> ");
        //System.out.println(InetAddress.getLocalHost().getHostAddress());
        snmp.send(pdu, communityTarget);
        snmp.close();
    }


}
