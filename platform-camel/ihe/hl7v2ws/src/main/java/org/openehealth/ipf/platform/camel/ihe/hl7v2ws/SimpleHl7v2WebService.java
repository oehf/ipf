/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws;

import org.openehealth.ipf.commons.ihe.hl7v2ws.SimpleHl7v2WsPortType;

/**
 * Web Service for HL7v2 components with a single operation.
 * @author Dmytro Rud
 */
abstract public class SimpleHl7v2WebService extends AbstractHl7v2WebService implements SimpleHl7v2WsPortType {

    @Override
    public String operation(String request) {
        return doProcess(request);
    }
}
