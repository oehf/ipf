/*
 * Copyright 2017 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hpd.iti58;

import org.openehealth.ipf.commons.ihe.hpd.iti58.Iti58PortType;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse;
import org.openehealth.ipf.platform.camel.ihe.hpd.HpdQueryEndpoint;
import org.openehealth.ipf.platform.camel.ihe.hpd.HpdQueryService;

public class Iti58Service extends HpdQueryService implements Iti58PortType {

    protected Iti58Service(HpdQueryEndpoint endpoint) {
        super(endpoint);
    }

    @Override
    public BatchResponse providerInformationQueryRequest(BatchRequest body) {
        return doProcess(body);
    }
}
