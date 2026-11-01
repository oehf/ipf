/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.core.responses;

import net.ihe.gazelle.hl7v3.datatypes.II;

/**
 * Acknowledgement properties shared by all simplified HL7v3 response models.
 * <p>
 * This is an interface rather than a base class because the query response models extend their respective query
 * model, as the query parameters are echoed in the response. The properties are implemented by the response classes.
 * The acknowledgement details are not part of this interface, because their type differs between the
 * acknowledgement (MCCI_MT000200UV01) and the query response (MCCI_MT000300UV01) wrappers.
 *
 * @author Christian Ohr
 * @since 6.1
 */
public interface Hl7v3Response {

    /**
     * @return the acknowledgement type code, e.g. "AA" (Application Accept), "AE" (Application Error) or
     * "AR" (Application Reject).
     */
    String getAcknowledgementTypeCode();

    void setAcknowledgementTypeCode(String acknowledgementTypeCode);

    /**
     * @return the ID of the message this response refers to.
     */
    II getTargetMessageId();

    void setTargetMessageId(II targetMessageId);

    /**
     * Sets the acknowledgement type code to "Application Accept".
     */
    default void setApplicationAccept() {
        setAcknowledgementTypeCode("AA");
    }

    /**
     * Sets the acknowledgement type code to "Application Error".
     */
    default void setApplicationError() {
        setAcknowledgementTypeCode("AE");
    }

    /**
     * Sets the acknowledgement type code to "Application Reject".
     */
    default void setApplicationReject() {
        setAcknowledgementTypeCode("AR");
    }
}
