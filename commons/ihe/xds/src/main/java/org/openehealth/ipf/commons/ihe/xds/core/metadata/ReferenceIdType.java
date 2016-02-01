/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import lombok.EqualsAndHashCode;

/**
 * @author Dmytro Rud
 */
@EqualsAndHashCode(callSuper = true)
public class ReferenceIdType extends XdsEnum {
    private static final long serialVersionUID = -4182700420856203082L;

    public static final ReferenceIdType UNIQUE_ID    = new ReferenceIdType(Type.OFFICIAL, "urn:ihe:iti:xds:2013:uniqueId");
    public static final ReferenceIdType ACCESSION    = new ReferenceIdType(Type.OFFICIAL, "urn:ihe:iti:xds:2013:accession");
    public static final ReferenceIdType REFERRAL     = new ReferenceIdType(Type.OFFICIAL, "urn:ihe:iti:xds:2013:referral");
    public static final ReferenceIdType ORDER        = new ReferenceIdType(Type.OFFICIAL, "urn:ihe:iti:xds:2013:order");
    public static final ReferenceIdType WORKFLOW_ID  = new ReferenceIdType(Type.OFFICIAL, "urn:ihe:iti:xdw:2013:workflowId");
    public static final ReferenceIdType ENCOUNTER_ID = new ReferenceIdType(Type.OFFICIAL, "urn:ihe:iti:xds:2015:encounterId");

    public static final ReferenceIdType[] OFFICIAL_VALUES = {
            UNIQUE_ID, ACCESSION, REFERRAL, ORDER, WORKFLOW_ID, ENCOUNTER_ID};

    public ReferenceIdType(Type type, String ebXML) {
        super(type, ebXML);
    }

    @Override
    public String getJaxbValue() {
        if (getType() == Type.OFFICIAL) {
            String s = getEbXML30();
            int pos = s.lastIndexOf(':');
            return s.substring(pos + 1);
        }
        return getEbXML30();
    }

}
