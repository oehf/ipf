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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.util.Terser;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;

/**
 * This exception is used to wrap one of more
 * {@link org.openehealth.ipf.modules.hl7.HL7v2Exception}s, e.g. as result for
 * validating a message that has errors in multiple places. It contains an own
 * error message, which {@link #populateMessage(Message, AckTypeCode)} writes
 * into MSA-3.
 * 
 * @author Christian Ohr
 * @author Marek Vaclavik
 * @deprecated Validation in HAPI 2.2 does not require composite exceptions
 * 
 */
@SuppressWarnings("serial")
public class CompositeHL7v2Exception extends AbstractHL7v2Exception implements
		Iterable<AbstractHL7v2Exception> {

	private final List<AbstractHL7v2Exception> wrapped;

	public CompositeHL7v2Exception() {
		this("HL7 Processing Error", new ArrayList<AbstractHL7v2Exception>());
	}

	public CompositeHL7v2Exception(List<AbstractHL7v2Exception> wrapped) {
		this("HL7 Processing Error", wrapped);
	}

	public CompositeHL7v2Exception(AbstractHL7v2Exception[] wrapped) {
		this("HL7 Processing Error", Arrays.asList(wrapped));
	}

	public CompositeHL7v2Exception(String message,
			AbstractHL7v2Exception[] wrapped) {
		this(message, Arrays.asList(wrapped));
	}

	public CompositeHL7v2Exception(String message) {
		this(message, new ArrayList<AbstractHL7v2Exception>());
	}

	public CompositeHL7v2Exception(String message,
			List<AbstractHL7v2Exception> wrapped) {
		super(message);
		this.wrapped = wrapped;
	}

	/**
	 * Adds an exception
	 * 
	 * @param exception
	 * @return <code>true</code> if successfully added, <code>false</code>
	 *         otherwise
	 */
	public boolean add(AbstractHL7v2Exception exception) {
		return wrapped.add(exception);
	}

	/**
	 * @see org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception#populateMessage(ca.uhn.hl7v2.model.Message,
	 *      org.openehealth.ipf.modules.hl7.AckTypeCode)
	 */
	@Override
	public Message populateMessage(Message m, AckTypeCode code) {
		try {
            if (MessageUtils.atLeastVersion(m, "2.5")) {
                Segment errorSegment = (Segment) m.get("ERR");
                fillErr347(errorSegment);
            } else {
                Segment msaSegment = (Segment) m.get("MSA");
                Terser.set(msaSegment, 3, 0, 1, 1, getMessage());
            }
            
            for (AbstractHL7v2Exception exception : wrapped) {
                exception.populateMessage(m, code);
            }

			return m;
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
    public Iterator<AbstractHL7v2Exception> iterator() {
		return wrapped.iterator();
	}

}
