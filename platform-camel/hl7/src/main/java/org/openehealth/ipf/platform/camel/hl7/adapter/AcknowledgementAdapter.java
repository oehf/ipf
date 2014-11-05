/*
 * Copyright 2014 the original author or authors.
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

package org.openehealth.ipf.platform.camel.hl7.adapter;

import ca.uhn.hl7v2.AcknowledgmentCode;
import ca.uhn.hl7v2.ErrorCode;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import org.apache.camel.util.ObjectHelper;

/**
 *
 */
public class AcknowledgementAdapter extends HapiAdapter {

    @Override
    protected Message doProcessMessage(Message message, Throwable t, Object... inputParams) {
        AcknowledgmentCode acknowledgementCode = inputParams != null && inputParams.length > 0 ?
                (AcknowledgmentCode) inputParams[0] :
                AcknowledgmentCode.AA;
        String errorMessage = inputParams != null && inputParams.length > 1 ?
                inputParams[1].toString() :
                "Error while processing HL7 message";
        ErrorCode errorCode = inputParams != null && inputParams.length > 2 ?
                (ErrorCode) inputParams[2] :
                ErrorCode.APPLICATION_INTERNAL_ERROR;
        try {
            HL7Exception hl7e = generateHL7Exception(t, acknowledgementCode, errorMessage, errorCode);
            if (t != null && acknowledgementCode == null) {
                acknowledgementCode = AcknowledgmentCode.AE;
            }
            return message.generateACK(acknowledgementCode == null ? AcknowledgmentCode.AA : acknowledgementCode, hl7e);
        } catch (Exception e) {
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }

    }

    private HL7Exception generateHL7Exception(Throwable t, AcknowledgmentCode acknowledgementCode, String errorMessage, ErrorCode errorCode) {
        if (t == null) {
            if (acknowledgementCode != null && !acknowledgementCode.name().endsWith("A")) {
                return new HL7Exception(errorMessage, errorCode);
            } else {
                return null;
            }
        }
        return t instanceof HL7Exception ?
                (HL7Exception) t :
                new HL7Exception(errorMessage != null ? errorMessage : t.getMessage(), errorCode, t);
    }

}
