/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.modules.hl7;

import ca.uhn.hl7v2.*;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.util.Terser;
import lombok.Delegate;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;

/**
 * HL7v2Exception extends {@link RuntimeException} so it need not to be caught.
 * All calls are delegated to the contained {@link ca.uhn.hl7v2.HL7Exception}
 * 
 * @author Christian Ohr
 * @author Marek Vaclavik
 */
@SuppressWarnings("serial")
public class HL7v2Exception extends RuntimeException {

	private HL7Exception nested;

	public HL7v2Exception(HL7Exception nested) {
        this.nested = nested;
	}

    Object getDetail() {
        return nested.getDetail();
    }

    public void setSegmentRepetition(int segmentRepetition) {
        nested.setSegmentRepetition(segmentRepetition);
    }

    public ErrorCode getError() {
        return nested.getError();
    }

    public void setFieldPosition(int pos) {
        nested.setFieldPosition(pos);
    }

    public void setError(ErrorCode errorCode) {
        nested.setError(errorCode);
    }

    public Message populateResponse(Message emptyResponse, AcknowledgmentCode acknowledgmentCode, int repetition) throws HL7Exception {
        return nested.populateResponse(emptyResponse, acknowledgmentCode, repetition);
    }

    public String getMessageWithoutLocation() {
        return nested.getMessageWithoutLocation();
    }

    public Severity getSeverity() {
        return nested.getSeverity();
    }

    public void setResponseMessage(Message responseMessage) {
        nested.setResponseMessage(responseMessage);
    }

    public void setDetail(Object detail) {
        nested.setDetail(detail);
    }

    public void setErrorCode(int errorCode) {
        nested.setErrorCode(errorCode);
    }

    public void setSeverity(Severity severity) {
        nested.setSeverity(severity);
    }

    public int getErrorCode() {
        return nested.getErrorCode();
    }

    public void setSegmentName(String segmentName) {
        nested.setSegmentName(segmentName);
    }

    public Message getResponseMessage() {
        return nested.getResponseMessage();
    }

    public void setLocation(Location location) {
        nested.setLocation(location);
    }

    public Location getLocation() {
        return nested.getLocation();
    }

    @Override
    public String getMessage() {
        return nested.getMessage();
    }
}
