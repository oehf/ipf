package org.openehealth.ipf.commons.ihe.xacml20.herasaf.functions;

import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CV;
import org.herasaf.xacml.core.function.AbstractFunction;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * @author Dmytro Rud
 */
public class CvEqualFunction extends AbstractFunction {

    public static final String ID = "urn:hl7-org:v3:function:CV-equal";

    @Override
    public String getFunctionId() {
        return ID;
    }

    @Override
    public Object handle(Object... args) throws FunctionProcessingException {
        if (args.length != 2) {
            throw new FunctionProcessingException("Invalid number of parameters.");
        }
        try {
            CV arg0 = (CV) args[0];
            CV arg1 = (CV) args[1];
            return arg0.getCode().equals(arg1.getCode()) && arg0.getCodeSystem().equals(arg1.getCodeSystem());

        } catch (ClassCastException e) {
            throw new FunctionProcessingException("The arguments are of the wrong datatype", e);
        } catch (Exception e) {
            throw new FunctionProcessingException(e);
        }
    }
}
