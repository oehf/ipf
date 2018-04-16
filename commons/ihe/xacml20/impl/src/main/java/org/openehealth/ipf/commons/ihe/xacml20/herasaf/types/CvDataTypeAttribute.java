package org.openehealth.ipf.commons.ihe.xacml20.herasaf.types;

import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CV;

/**
 * @author Dmytro Rud
 */
public class CvDataTypeAttribute extends Hl7v3DataTypeAttribute<CV> {

    public static final String ID = "urn:hl7-org:v3#CV";

    public CvDataTypeAttribute() {
        super(CV.class);
    }

    @Override
    public String getDatatypeURI() {
        return ID;
    }

}
