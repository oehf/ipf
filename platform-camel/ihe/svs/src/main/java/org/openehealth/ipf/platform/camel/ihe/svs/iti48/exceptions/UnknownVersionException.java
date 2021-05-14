package org.openehealth.ipf.platform.camel.ihe.svs.iti48.exceptions;

import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapFault;

import javax.xml.namespace.QName;

public class UnknownVersionException extends SoapFault {

    public UnknownVersionException() {
        super("Version unknown", Soap12.getInstance().getReceiver());
        this.setSubCode(new QName(null, "VERUNK"));
    }
}
