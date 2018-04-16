package org.openehealth.ipf.commons.ihe.xacml20.herasaf;

import org.openehealth.ipf.commons.ihe.xacml20.herasaf.functions.CvEqualFunction;
import org.openehealth.ipf.commons.ihe.xacml20.herasaf.functions.IiEqualFunction;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.functions.AbstractFunctionsJaxbTypeAdapterInitializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class Hl7v3FunctionsInitializer extends AbstractFunctionsJaxbTypeAdapterInitializer {

    @Override
    protected Map<String, Function> createTypeInstances() {
        List<Function> functions = createInstances(IiEqualFunction.class, CvEqualFunction.class);

        Map<String, Function> instancesMap = new HashMap<>();
        for (Function function : functions) {
            instancesMap.put(function.toString(), function);
        }

        return instancesMap;
    }
}
