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

import java.util.Map;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.AssociationType1;

/**
 * Transforms an {@link Association} to its ebXML 2.1 representation.
 * @author Jens Riemschneider
 */
public class AssociationTransformer {
    /**
     * Transforms the given association to its ebXML 2.1 representation.
     * @param knownObjects
     *          map of known objects and their IDs. This map must contain the objects
     *          referred to by the association.
     * @param association
     *          the association to transform.
     * @return the ebXML 2.1 representation.
     */
    public AssociationType1 toEbXML21(Map<String, Object> knownObjects, Association association) {
        if (association == null) {
            return null;
        }
        
        AssociationType1 result = Ebrs21.createAssociation();
        AssociationType type = association.getAssociationType();
        result.setAssociationType(type != null ? type.getRepresentation() : null);
        result.setSourceObject(knownObjects.get(association.getSourceUUID()));
        result.setTargetObject(knownObjects.get(association.getTargetUUID()));
        
        return result;
    }
    
    /**
     * Transforms the given ebXML 2.1 representation into an {@link Association}. 
     * @param knownObjects
     *          map of known objects and their IDs. This map must contain the objects
     *          referred to by the association.
     * @param association
     *          the ebXML 2.1 association.
     * @return the created {@link Association} instance.
     */
    public Association fromEbXML21(Map<Object, String> knownObjects, AssociationType1 association) {
        if (association == null) {
            return null;
        }
        
        Association result = new Association();
        result.setAssociationType(AssociationType.valueOfRepresentation(association.getAssociationType()));
        result.setTargetUUID(knownObjects.get(association.getTargetObject()));
        result.setSourceUUID(knownObjects.get(association.getSourceObject()));
        
        return result;
    }
}
