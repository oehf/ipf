package org.openehealth.ipf.commons.ihe.xacml20.herasaf.types;

import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.II;

/**
 * @author Dmytro Rud
 */
public class IiDataTypeAttribute extends Hl7v3DataTypeAttribute<II> {

    public static final String ID = "urn:hl7-org:v3#II";

    public IiDataTypeAttribute() {
        super(II.class);
    }

    @Override
    public String getDatatypeURI() {
        return ID;
    }

}
