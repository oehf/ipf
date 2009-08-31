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
package org.openehealth.ipf.commons.ihe.xds.transform.ebxml;

import org.openehealth.ipf.commons.ihe.xds.metadata.Recipient;
import org.openehealth.ipf.commons.ihe.xds.transform.hl7.OrganizationTransformer;
import org.openehealth.ipf.commons.ihe.xds.transform.hl7.PersonTransformer;

/**
 * Transforms between a {@link Recipient} and its ebXML representation.
 * <p>
 * The ebXML representation is a simple String used as a slot value.
 * This class is independent of the ebXML version being used.
 * @author Jens Riemschneider
 */
public class RecipientTransformer {
    private final PersonTransformer personTransformer = new PersonTransformer();
    private final OrganizationTransformer organizationTransformer = new OrganizationTransformer();
    
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
        
        String person = personTransformer.toHL7(recipient.getPerson());
        String organization = organizationTransformer.toHL7(recipient.getOrganization());
        
        if (person == null) {
            return organization;
        }
        
        if (organization == null) {
            return "|" + person;
        }
        
        return organization + "|" + person;
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
        if (parts.length > 1) {
            recipient.setOrganization(organizationTransformer.fromHL7(parts[0]));
            recipient.setPerson(personTransformer.fromHL7(parts[1]));
        }
        else {
            recipient.setOrganization(organizationTransformer.fromHL7(parts[0]));
        }
        
        return recipient;
    }
}
