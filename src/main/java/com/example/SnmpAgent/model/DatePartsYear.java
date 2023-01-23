package com.example.SnmpAgent.model;

import org.snmp4j.agent.MOAccess;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

import java.time.LocalDate;

public class DatePartsYear<V extends Variable> extends CustomManagedObject {

    public DatePartsYear(OID oid, MOAccess access) {
        super(oid, access, null);
    }

    @Override
    public V getValue() {

        int result = LocalDate.now().getYear();

        return (V) (new Integer32(result));
    }
}
