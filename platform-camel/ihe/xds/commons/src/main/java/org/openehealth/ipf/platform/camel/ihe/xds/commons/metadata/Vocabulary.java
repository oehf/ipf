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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata;

/**
 * List of XDS related vocabulary constants.
 * @author Jens Riemschneider
 */
public abstract class Vocabulary {
    private Vocabulary() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }
    
    /** Universal ID Type to be used for all HD data types used with XDS */
    public static final String UNIVERSAL_ID_TYPE_OID = "ISO";
    
    /** XDSDocumentEntry classification node */
    public static final String DOC_ENTRY_CLASS_NODE = 
        "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";
    
    /** Author External Classification Scheme of the Document Entry */
    public static final String DOC_ENTRY_AUTHOR_CLASS_SCHEME = 
        "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";

    /** Name of the slot that is used for author persons */
    public static final String SLOT_NAME_AUTHOR_PERSON = "authorPerson";
    
    /** Name of the slot that is used for author institutions */
    public static final String SLOT_NAME_AUTHOR_INSTITUTION = "authorInstitution";
        
    /** Name of the slot that is used for author roles */
    public static final String SLOT_NAME_AUTHOR_ROLE = "authorRole";

    /** Name of the slot that is used for author specialties */
    public static final String SLOT_NAME_AUTHOR_SPECIALTY = "authorSpecialty";
}
