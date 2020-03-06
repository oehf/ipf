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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55.deferredresponse;

import org.apache.camel.ExchangePattern;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3NakFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.Iti55Utils;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.asyncresponse.Iti55DeferredResponsePortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractAsyncResponseWebService;

/**
 * Service implementation for the ITI-55 XCPD Initiating Gateway actor
 * (receiver of deferred responses).
 * @author Dmytro Rud
 */
public class Iti55DeferredResponseService extends AbstractAsyncResponseWebService implements Iti55DeferredResponsePortType {

    @Override
    protected boolean canDropCorrelation(Object response) {
        return (! Iti55Utils.isMcciAck((String) response));
    }

    @Override
    public String receiveDeferredResponse(String responseString) {
        process(responseString, null, ExchangePattern.InOnly);
        return Hl7v3NakFactory.response(responseString,
                                        null,
                                        "MCCI_IN000002UV01",
                                        null,
                                        false,
                                        false);
    }

    @Override
    protected String[] getAlternativeResponseKeys(Object response) {
        return new String[] { Iti55Utils.responseQueryId(Hl7v3Utils.slurp((String) response)) };
    }
}
