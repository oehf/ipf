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

import ca.uhn.hl7v2.model.Message;

/**
 * Abstract base exception class for HL7 message processing. In contrast to
 * HAPI's HL7 exception it extends RuntimetimeException and therefore need
 * not not be caught.
 * 
 * @author Christian Ohr
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractHL7v2Exception extends RuntimeException {

	private int errCode = 207;

	public AbstractHL7v2Exception() {
	}

	public AbstractHL7v2Exception(String message) {
		super(message);
	}

	public AbstractHL7v2Exception(String message, int errCode) {
		super(message);
		this.errCode = errCode;
	}

	public AbstractHL7v2Exception(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AbstractHL7v2Exception(String message, int errCode, Throwable cause) {
		super(message, cause);
		this.errCode = errCode;
	}
	
	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrorMessage() {
		return HL70357.messageFor(getErrCode());
	}

	/**
	 * Populates the respective ERR fields of a response message with data
	 * contained in this exception
	 * 
	 * @param m the empty HAPI message
	 * @param code the ack code
	 * @return the populated HAPI message
	 */
	protected abstract Message populateMessage(Message m, AckTypeCode code);

}
