package org.openehealth.ipf.commons.ihe.xacml20.herasaf;

import org.openehealth.ipf.commons.ihe.xacml20.herasaf.types.CvDataTypeAttribute;
import org.openehealth.ipf.commons.ihe.xacml20.herasaf.types.IiDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.datatypes.AbstractDataTypesJaxbTypeAdapterInitializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class Hl7v3DataTypesInitializer extends AbstractDataTypesJaxbTypeAdapterInitializer {

    @Override
    protected Map<String, DataTypeAttribute<?>> createTypeInstances() {
        List<DataTypeAttribute<?>> instances = createInstances(IiDataTypeAttribute.class, CvDataTypeAttribute.class);
        Map<String, DataTypeAttribute<?>> instancesMap = new HashMap<>();
        for (DataTypeAttribute<?> dataTypeAttribute : instances) {
            instancesMap.put(dataTypeAttribute.getDatatypeURI(), dataTypeAttribute);
        }
        return instancesMap;
    }

}
