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

        try {
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

            pdu.add(new VariableBinding(SnmpConstants.sysDescr, new OctetString("SYNC_REQUEST")));
            pdu.add(new VariableBinding(SnmpConstants.sysName, new OctetString("WORKSTATION")));
            pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(InetAddress.getLocalHost().getHostAddress())));
            pdu.add(new VariableBinding(SnmpConstants.sysUpTime, new OctetString(new Date().toString())));

            WindowsMIB mib = new WindowsMIB();
            WindowsConverter converter = new WindowsConverter();
            WindowsObject win = converter.getConvertedData();


            pdu.add(new VariableBinding(new OID(mib.SO_OID), new OctetString(win.getSistemaOperacional())));
            pdu.add(new VariableBinding(new OID(mib.ARQUITETURA_SO_OID), new OctetString(String.valueOf(win.getArquiteturaSo()))));
            pdu.add(new VariableBinding(new OID(mib.FABRICANTE_OID), new OctetString(win.getFabricante())));
            pdu.add(new VariableBinding(new OID(mib.MODELO_OID), new OctetString(win.getModelo())));
            pdu.add(new VariableBinding(new OID(mib.NUMERO_SERIE_OID), new OctetString(win.getNumeroSerie())));
            pdu.add(new VariableBinding(new OID(mib.PROCESSADOR_OID), new OctetString(win.getProcessador())));
            pdu.add(new VariableBinding(new OID(mib.MEMORIA_RAM_OID), new OctetString(win.getMemoriaRam())));
            pdu.add(new VariableBinding(new OID(mib.NOME_OID), new OctetString(win.getNomeHost())));
            pdu.add(new VariableBinding(new OID(mib.DOMINIO_OID), new OctetString(win.getDominio())));
            pdu.add(new VariableBinding(new OID(mib.USUARIO_LOGADO_OID), new OctetString(win.getUltimoUsuarioLogado())));
            pdu.add(new VariableBinding(new OID(mib.TEMPO_LIGADO_OID), new OctetString(win.getTempoLigado())));
            pdu.add(new VariableBinding(new OID(mib.GATEWAY_OID), new OctetString(win.getGateway())));
            pdu.add(new VariableBinding(new OID(mib.DNS_OID), new OctetString(win.getDnsList().toString())));
            pdu.add(new VariableBinding(new OID(mib.INTERFACES_OID), new OctetString(win.getIntefaces().toString())));
            pdu.add(new VariableBinding(new OID(mib.DISCO_RIGIDO_OID), new OctetString(win.getDiscos().toString())));
            pdu.add(new VariableBinding(new OID(mib.IMPRESSORAS_OID), new OctetString(win.getImpressoras().toString())));
            pdu.add(new VariableBinding(new OID(mib.PLACAS_VIDEO_OID), new OctetString(win.getPlascasVideo().toString())));
            pdu.add(new VariableBinding(new OID(mib.PROGRAMAS_OID), new OctetString(win.getProgramasIntalados().toString())));


            pdu.setType(PDU.NOTIFICATION);

            Snmp snmp = new Snmp(transportMapping);
            System.out.println("Enviando armadilha pdu v1 ---> ");
            //System.out.println(InetAddress.getLocalHost().getHostAddress());
            snmp.send(pdu, communityTarget);
            snmp.close();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
