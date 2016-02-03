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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import lombok.EqualsAndHashCode;

/**
 * Lists all possible types of associations between two documents.
 *
 * @author Jens Riemschneider
 */
@EqualsAndHashCode(callSuper = true)
public class AssociationType extends XdsVersionedEnum {
    private static final long serialVersionUID = 3179632406699830867L;

    /** An entry that is appended to another one. */
    public static final AssociationType APPEND = new AssociationType(Type.OFFICIAL, "APND", "urn:ihe:iti:2007:AssociationType:APND");
    /** An entry that replaced another one. */
    public static final AssociationType REPLACE = new AssociationType(Type.OFFICIAL, "RPLC", "urn:ihe:iti:2007:AssociationType:RPLC");
    /** An entry that transforms another one. */
    public static final AssociationType TRANSFORM = new AssociationType(Type.OFFICIAL, "XFRM", "urn:ihe:iti:2007:AssociationType:XFRM");
    /** An entry that transforms and replaces another one. */
    public static final AssociationType TRANSFORM_AND_REPLACE = new AssociationType(Type.OFFICIAL, "XFRM_RPLC", "urn:ihe:iti:2007:AssociationType:XFRM_RPLC");
    /** An entry that is a member of another one. */
    public static final AssociationType HAS_MEMBER = new AssociationType(Type.OFFICIAL, "HasMember", "urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember");
    /** An entry that represents a signature of another one. */
    public static final AssociationType SIGNS = new AssociationType(Type.OFFICIAL, "signs", "urn:ihe:iti:2007:AssociationType:signs");
    /** An entry that represents a link to the On-Demand DocumentEntry. */
    public static final AssociationType IS_SNAPSHOT_OF = new AssociationType(Type.OFFICIAL, "IsSnapshotOf", "urn:ihe:iti:2010:AssociationType:IsSnapshotOf");
    /** An entry that represents an association for update availability status trigger. */
    public static final AssociationType UPDATE_AVAILABILITY_STATUS = new AssociationType(Type.OFFICIAL, "UpdateAvailabilityStatus", "urn:ihe:iti:2010:AssociationType:UpdateAvailabilityStatus");
    /** An entry that represents an association for submit association trigger. */
    public static final AssociationType SUBMIT_ASSOCIATION = new AssociationType(Type.OFFICIAL, "SubmitAssociation", "urn:ihe:iti:2010:AssociationType:SubmitAssociation");

    public static final AssociationType[] OFFICIAL_VALUES = {
            APPEND, REPLACE, TRANSFORM, TRANSFORM_AND_REPLACE, HAS_MEMBER, SIGNS,
            IS_SNAPSHOT_OF, UPDATE_AVAILABILITY_STATUS, SUBMIT_ASSOCIATION};

    public AssociationType(Type type, String ebXML21, String ebXML30) {
        super(type, ebXML21, ebXML30);
    }

    /**
     * @return <code>true</code> if the association contains a replacement.
     */
    public boolean isReplace() {
        return this == REPLACE || this == TRANSFORM_AND_REPLACE;
    }

    @Override
    public String getJaxbValue() {
        return getEbXML21();
    }
}
