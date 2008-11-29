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
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.util.Terser;

/**
 * Alternative HL7 Exception. Compared to the HAPI HL7Exception, it adds
 * several location parameters and correctly populates the ERR segment of a
 * response message depending on the message version. Furthermore it extends
 * {@link RuntimeException} so it need not to be caught.
 * 
 * @author Christian Ohr
 * @author Marek Václavík
 */
@SuppressWarnings("serial")
public class HL7v2Exception extends AbstractHL7v2Exception {

	private static final ErrorLocation EMPTY_LOCATION = new ErrorLocation();

	private ErrorLocation location;

	public HL7v2Exception() {
		location = EMPTY_LOCATION;
	}

	public HL7v2Exception(String message, int errCode, Throwable cause, ErrorLocation location) {
		super(message, errCode, cause);
		this.location = location;
	}

	public HL7v2Exception(String message, int errCode, Throwable cause) {
		this(message, errCode, cause, EMPTY_LOCATION);
	}
	
	public HL7v2Exception(String message, int errCode) {
		this(message, errCode, null, EMPTY_LOCATION);
	}

	public HL7v2Exception(String message) {
		this(message, 207, null, EMPTY_LOCATION);
	}


	public HL7v2Exception(String message, ErrorLocation location) {
		this(message, 207, null, location);
	}

	public HL7v2Exception(ErrorLocation location) {
		this(null, 207, null, location);
	}

	public HL7v2Exception(ca.uhn.hl7v2.HL7Exception exception) {
		this(exception.getMessage(), 207, exception);
		ErrorLocation location = new ErrorLocation();
		location.setFieldPosition(exception.getFieldPosition());
		location.setSegmentName(exception.getSegmentName());
		location.setSegmentRepetition(exception.getSegmentRepetition());
		setLocation(location);
	}

	public ErrorLocation getLocation() {
		return location;
	}

	public void setLocation(ErrorLocation location) {
		this.location = location;
	}

	/**
	 * @see org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception#populateMessage(ca.uhn.hl7v2.model.Message,
	 *      org.openehealth.ipf.modules.hl7.AckTypeCode)
	 */
	@Override
	protected Message populateMessage(Message m, AckTypeCode code) {

		try {
			Segment errorSegment = (Segment) m.get("ERR");
			if (m.getVersion().compareTo("2.5") < 0) {
				populateErr1(errorSegment);
			} else {
				populateErr2347(errorSegment);
			}
			Segment msaSegment = (Segment) m.get("MSA");
			Terser.set(msaSegment, 1, 0, 1, 1, code.name());
			return m;
		} catch (ca.uhn.hl7v2.HL7Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	/**
	 * Fills ERR-1 for messages version 2.4 and earlier
	 * 
	 * @param errorSegment
	 * @throws ca.uhn.hl7v2.HL7Exception
	 */
	protected void populateErr1(Segment errorSegment)
			throws ca.uhn.hl7v2.HL7Exception {
		int rep = errorSegment.getField(1).length;

		if (getLocation().getSegmentName() != null)
			Terser.set(errorSegment, 1, rep, 1, 1, getLocation()
					.getSegmentName());
		if (getLocation().getSegmentRepetition() >= 0)
			Terser.set(errorSegment, 1, rep, 2, 1, String.valueOf(getLocation()
					.getSegmentRepetition()));
		if (getLocation().getFieldPosition() >= 0)
			Terser.set(errorSegment, 1, rep, 3, 1, String.valueOf(getLocation()
					.getFieldPosition()));

		Terser.set(errorSegment, 1, rep, 4, 1, String.valueOf(getErrCode()));
		Terser.set(errorSegment, 1, rep, 4, 2, getErrorMessage());
		Terser.set(errorSegment, 1, rep, 4, 3, "HL70357");
		Terser.set(errorSegment, 1, rep, 4, 5, this.getMessage());
	}

	/**
	 * Fills ERR-2, 3, 4 for messages version 2.5 and later
	 * 
	 * @param errorSegment
	 */
	protected void populateErr2347(Segment errorSegment)
			throws ca.uhn.hl7v2.HL7Exception {
		int rep = errorSegment.getField(2).length;

		// Fill error location (ERR-2) with member variables
		if (getLocation().getSegmentName() != null)
			Terser.set(errorSegment, 2, rep, 1, 1, getLocation()
					.getSegmentName());
		if (getLocation().getSegmentRepetition() >= 0)
			Terser.set(errorSegment, 2, rep, 2, 1, String.valueOf(getLocation()
					.getSegmentRepetition()));
		if (getLocation().getFieldPosition() >= 0)
			Terser.set(errorSegment, 2, rep, 3, 1, String.valueOf(getLocation()
					.getFieldPosition()));
		if (getLocation().getFieldPosition() >= 0)
			Terser.set(errorSegment, 2, rep, 4, 1, String.valueOf(getLocation()
					.getFieldRepetition()));
		if (getLocation().getComponentNumber() >= 0)
			Terser.set(errorSegment, 2, rep, 5, 1, String.valueOf(getLocation()
					.getComponentNumber()));
		if (getLocation().getSubcomponentNumber() >= 0)
			Terser.set(errorSegment, 2, rep, 6, 1, String.valueOf(getLocation()
					.getSubcomponentNumber()));

		Terser.set(errorSegment, 3, 0, 1, 1, String.valueOf(getErrCode()));
		Terser.set(errorSegment, 3, 0, 2, 1, getErrorMessage());
		Terser.set(errorSegment, 3, 0, 3, 1, "HL70357");
		Terser.set(errorSegment, 3, 0, 5, 1, getMessage());

		Terser.set(errorSegment, 4, 0, 1, 1, "E");
		Terser.set(errorSegment, 7, 0, 1, 1, getMessage());

	}

}
