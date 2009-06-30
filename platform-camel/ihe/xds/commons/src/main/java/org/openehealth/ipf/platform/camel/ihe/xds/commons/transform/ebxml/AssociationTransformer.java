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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml;

import static org.apache.commons.lang.Validate.notNull;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAssociation;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;

/**
 * Transforms an {@link Association} to its ebXML representation.
 * @author Jens Riemschneider
 */
public class AssociationTransformer {
    private final EbXMLFactory factory;
    
    public AssociationTransformer(EbXMLFactory factory) {
        notNull(factory, "factory cannot be null");
        this.factory = factory;
    }
    
    /**
     * Transforms the given association to its ebXML representation.
     * @param association
     *          the association to transform.
     * @param objectLibrary 
     *          the object library.
     * @return the ebXML representation.
     */
    public EbXMLAssociation toEbXML(Association association, ObjectLibrary objectLibrary) {
        notNull(objectLibrary, "objectLibrary cannot be null");
        if (association == null) {
            return null;
        }
        
        EbXMLAssociation result = factory.createAssociation(objectLibrary);
        result.setAssociationType(association.getAssociationType());
        result.setSource(association.getSourceUUID());
        result.setTarget(association.getTargetUUID());
        
        return result;
    }
    
    /**
     * Transforms the given ebXML representation into an {@link Association}. 
     * @param association
     *          the ebXML association.
     * @return the created {@link Association} instance.
     */
    public Association fromEbXML(EbXMLAssociation association) {
        if (association == null) {
            return null;
        }
        
        Association result = new Association();
        result.setAssociationType(association.getAssociationType());
        result.setTargetUUID(association.getTarget());
        result.setSourceUUID(association.getSource());
        
        return result;
    }
}
