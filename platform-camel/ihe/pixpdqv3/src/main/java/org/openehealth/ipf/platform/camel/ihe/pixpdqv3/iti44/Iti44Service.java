/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti44;

import org.openehealth.ipf.commons.ihe.pixpdqv3.iti44.Iti44PixPortType;
import org.openehealth.ipf.commons.ihe.pixpdqv3.iti44.Iti44XdsPortType;
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.DefaultHL7v3WebService;

/**
 * Generic service class for the IHE ITI-44 transaction.
 * @author Dmytro Rud
 */
public class Iti44Service extends DefaultHL7v3WebService implements Iti44XdsPortType, Iti44PixPortType {

    @Override
    public String documentRegistryPRPAIN201301UV02(String body) {
        return doProcess(body);
    }

    @Override
    public String documentRegistryPRPAIN201302UV02(String body) {
        return doProcess(body);
    }

    @Override
    public String documentRegistryPRPAIN201304UV02(String body) {
        return doProcess(body);
    }

    @Override
    public String pixManagerPRPAIN201301UV02(String body) {
        return doProcess(body);
    }

    @Override
    public String pixManagerPRPAIN201302UV02(String body) {
        return doProcess(body);
    }

    @Override
    public String pixManagerPRPAIN201304UV02(String body) {
        return doProcess(body);
    }
}
