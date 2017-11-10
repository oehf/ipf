/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pam.v25.segment;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.datatype.NM;
import ca.uhn.hl7v2.model.v25.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import org.openehealth.ipf.modules.hl7.HL7v2Exception;

/**
 * The ZZI segment is intended to be used for information that support distributed
 * tracing across system boundaries using Zipkin. Also see http://zipkin.io/pages/instrumenting.html
 * for details
 * 
 * @author Christian Ohr
 */
@SuppressWarnings("serial")
public class ZZI extends AbstractSegment {

    /**
     * @param parent
     * @param factory
     */
    public ZZI(Group parent, ModelClassFactory factory) {
        super(parent, factory);
        Message message = getMessage();
        try {
            add(ST.class, true, 1, 8, new Object[] { message }, "Trace ID");
            add(ST.class, true, 1, 8, new Object[] { message }, "Span ID");
            add(ST.class, false, 1, 8, new Object[] { message }, "Parent Span ID");
            add(NM.class, false, 1, 1, new Object[] { message }, "Sampled");
            add(NM.class, false, 1, 1, new Object[] { message }, "Flag");
        } catch (HL7Exception he) {
            throw new HL7v2Exception(he);
        }
    }

    /**
     * Returns trace ID (ZZI-1).
     *
     * @return trace ID
     */
    public ST getTraceID() {
        return getTypedField(1, 0);
    }

    /**
     * Returns span ID (ZZI-2).
     *
     * @return span ID
     */
    public ST getSpanID() {
        return getTypedField(2, 0);
    }

    /**
     * Returns parent span ID (ZZI-3).
     *
     * @return parent span ID
     */
    public ST getParentSpanID() {
        return getTypedField(3, 0);
    }

    /**
     * Returns the flag whether this span is to be sampled (ZZI-4).
     *
     * @return sample indicator
     */
    public NM getSampled() {
        return getTypedField(4, 0);
    }

    /**
     * Returns flag whether debugging shall be turned on (ZZI-5).
     *
     * @return debug flag
     */
    public NM getFlags() {
        return getTypedField(5, 0);
    }

}
