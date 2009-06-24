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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs21;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.hl7.HL7;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.hl7.HL7Delimiter;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.hl7.AssigningAuthorityTransformer;

/**
 * Transforms between an {@link Identifiable} and its ebXML 2.1 representation.
 * @author Jens Riemschneider
 */
public class IdentifiableTransformer {
    private final AssigningAuthorityTransformer assigningAuthorityTransformer = new AssigningAuthorityTransformer();
    
    /**
     * Transforms an {@link Identifiable} to its ebXML 2.1 representation.
     * @param identifiable
     *          the identifiable instance.
     * @return the ebXML 2.1 representation.
     */
    public String toEbXML21(Identifiable identifiable) {
        if (identifiable == null) {
            return null;
        }
        
        String hl7HD = assigningAuthorityTransformer.toHL7(identifiable.getAssigningAuthority());
        
        String hl7cx = HL7.render(HL7Delimiter.COMPONENT, 
            HL7.escape(identifiable.getId()),
            null,
            null,
            hl7HD);
        
        return hl7cx != null ? hl7cx : "";
    }

    /**
     * Transforms an ebXML string into an {@link Identifiable}.
     * @param ebXML
     *          the ebXML 2.1 representation of the value.
     * @return the identifiable instance.
     */
    public Identifiable fromEbXML21(String ebXML) {
        if (ebXML == null) {
            return null;
        }
        
        Identifiable identifiable = new Identifiable();
        List<String> cx = HL7.parse(HL7Delimiter.COMPONENT, ebXML);
        identifiable.setId(HL7.get(cx, 1, true));       
        identifiable.setAssigningAuthority(assigningAuthorityTransformer.fromHL7(HL7.get(cx, 4, false)));
        return identifiable;
    }    
}
