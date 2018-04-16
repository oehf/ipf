/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
