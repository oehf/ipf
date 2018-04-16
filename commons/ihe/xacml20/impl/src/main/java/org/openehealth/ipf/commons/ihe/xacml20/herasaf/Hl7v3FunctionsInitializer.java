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
