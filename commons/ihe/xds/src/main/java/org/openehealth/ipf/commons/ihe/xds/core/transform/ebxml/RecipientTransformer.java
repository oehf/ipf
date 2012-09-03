/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

/**
 * Transforms between a {@link Recipient} and its ebXML representation.
 * <p>
 * The ebXML representation is a simple String used as a slot value.
 * This class is independent of the ebXML version being used.
 * @author Jens Riemschneider
 */
public class RecipientTransformer {

    /**
     * Transforms a recipient into its ebXML representation (a slot value).
     * @param recipient
     *          the recipient to transform. Can be <code>null</code>.
     * @return the slot value. <code>null</code> if the input was <code>null</code>.
     */
    public String toEbXML(Recipient recipient) {
        if (recipient == null) {
            return null;
        }
        
        String person = Hl7v2Based.render(recipient.getPerson());
        String organization = Hl7v2Based.render(recipient.getOrganization());
        String telecom = Hl7v2Based.render(recipient.getTelecom());

        if ((person == null) && (organization == null) && (telecom == null)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        if (organization != null) {
            sb.append(organization);
        }
        if ((person != null) || (telecom != null)) {
            sb.append('|');
            if (person != null) {
                sb.append(person);
            }
            if (telecom != null) {
                sb.append('|').append(telecom);
            }
        }

        return sb.toString();
    }

    /**
     * Transforms an ebXML representation (a slot value) into a {@link Recipient}.
     * @param slotValue
     *          the slot value. Can be <code>null</code>.
     * @return the recipient. <code>null</code> if the input was <code>null</code>.
     */
    public Recipient fromEbXML(String slotValue) {
        if (slotValue == null || slotValue.isEmpty()) {
            return null;
        }
        
        Recipient recipient = new Recipient();

        String[] parts = slotValue.split("\\|");
        recipient.setOrganization(Hl7v2Based.parse(parts[0], Organization.class));
        if (parts.length > 1) {
            recipient.setPerson(Hl7v2Based.parse(parts[1], Person.class));
        }
        if (parts.length > 2) {
            recipient.setTelecom(Hl7v2Based.parse(parts[2], Telecom.class));
        }

        return recipient;
    }
}
