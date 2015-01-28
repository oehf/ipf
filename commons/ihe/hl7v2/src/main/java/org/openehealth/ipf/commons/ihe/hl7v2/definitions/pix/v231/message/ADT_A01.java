/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v231.message;

import ca.uhn.hl7v2.parser.ModelClassFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v231.group.ADT_A01_INSURANCE;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v231.group.ADT_A01_PROCEDURE;

public class ADT_A01 extends ADT_AXX<ADT_A01_PROCEDURE, ADT_A01_INSURANCE> {

    public ADT_A01() {
        super();
    }

    public ADT_A01(ModelClassFactory factory) {
        super(factory);
    }

    @Override
    protected Class<ADT_A01_PROCEDURE> procedureClass() {
        return ADT_A01_PROCEDURE.class;
    }

    @Override
    protected Class<ADT_A01_INSURANCE> insuranceClass() {
        return ADT_A01_INSURANCE.class;
    }
}
