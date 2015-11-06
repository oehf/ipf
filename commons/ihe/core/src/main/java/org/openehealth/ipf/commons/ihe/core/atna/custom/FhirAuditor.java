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
package org.openehealth.ipf.commons.ihe.core.atna.custom;

import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.ihe.QueryEvent;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

import java.io.UnsupportedEncodingException;
import java.util.Collections;

import static org.openehealth.ipf.commons.ihe.core.atna.custom.CustomAuditorUtils.configureEvent;

/**
 * Implementation of Fhir Auditors to send audit messages for
 * <ul>
 * <li>ITI-83 (PIXM Query)</li>
 * </ul>
 *
 * @author Christian Ohr
 */
public class FhirAuditor extends IHEAuditor {

    public static FhirAuditor getAuditor() {
        AuditorModuleContext ctx = AuditorModuleContext.getContext();
        return (FhirAuditor) ctx.getAuditor(FhirAuditor.class);
    }

    public void auditIti83(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String pixManagerUri,
            String clientIpAddress,
            String queryString) {
        if (!isAuditorEnabled()) {
            return;
        }

        QueryEvent event = new QueryEvent(
                true,
                eventOutcome,
                new IHETransactionEventTypeCodes.PIXMQuery(),
                Collections.<CodedValueType>emptyList());

        configureEvent(this, serverSide, event, null, null, pixManagerUri, pixManagerUri, clientIpAddress);
        event.addQueryParticipantObject("PIXmQuery", null, payloadBytes(queryString), null,
                new IHETransactionEventTypeCodes.PIXMQuery());
        audit(event);
    }


    protected static byte[] payloadBytes(String payload) {
        if (payload == null) {
            return null;
        }
        try {
            return payload.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return payload.getBytes();
        }
    }


}
