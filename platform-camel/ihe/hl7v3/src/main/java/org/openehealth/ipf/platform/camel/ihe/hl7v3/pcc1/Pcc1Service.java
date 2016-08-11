/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.pcc1;

import org.openehealth.ipf.commons.ihe.hl7v3.pcc1.Pcc1PortType;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.AbstractHl7v3WebService;

import static org.openehealth.ipf.commons.ihe.hl7v3.QED.Interactions.PCC_1;

/**
 * Service implementation for the IHE PCC-1 transaction (QED).
 *
 * @author Dmytro Rud
 */
public class Pcc1Service extends AbstractHl7v3WebService implements Pcc1PortType {

    public Pcc1Service() {
        super(PCC_1);
    }

    @Override
    public String operation(String request) {
        return doProcess(request);
    }

    @Override
    public String continuation(String request) {
        return doProcess(request);
    }

    @Override
    public String cancel(String request) {
        return doProcess(request);
    }
}
