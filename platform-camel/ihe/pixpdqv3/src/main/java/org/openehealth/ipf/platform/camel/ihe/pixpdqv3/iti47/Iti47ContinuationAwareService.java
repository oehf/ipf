/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti47;

import org.openehealth.ipf.commons.ihe.pixpdqv3.iti47.Iti47PortType;
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.Hl7v3ContinuationStorage;
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.Hl7v3ContinuationAwareWebService;

// The main purpose of this class is to add "implements Iti47PortType"
// to the class Hl7v3ContinuationAwareWebService, as required by CXF.

/**
 * Continuation-Aware service implementation for the IHE ITI-47 transaction (PDQ v3).
 * @author Dmytro Rud
 */
public class Iti47ContinuationAwareService
        extends Hl7v3ContinuationAwareWebService
        implements Iti47PortType
{
    public Iti47ContinuationAwareService(
            Hl7v3ContinuationStorage storage,
            int defaultThreshold,
            boolean validationOnContinuation)
    {
        super(Iti47Endpoint.ITI_47, storage, defaultThreshold, validationOnContinuation);
    }
}
