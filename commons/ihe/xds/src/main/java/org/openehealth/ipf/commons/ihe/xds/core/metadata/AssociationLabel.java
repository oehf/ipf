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
 * Association labeling values used for the associations of submission sets.
 * @author Jens Riemschneider
 */
@EqualsAndHashCode(callSuper = true)
public class AssociationLabel extends XdsEnum {
    private static final long serialVersionUID = -1023207056531610239L;

    /** Label for associations to documents that are contained in the request. */
    public static final AssociationLabel ORIGINAL = new AssociationLabel(Type.OFFICIAL, "Original");
    /** Label for associations to documents that are only referenced in the request. */
    public static final AssociationLabel REFERENCE = new AssociationLabel(Type.OFFICIAL, "Reference");

    public static final AssociationLabel[] OFFICIAL_VALUES = {ORIGINAL, REFERENCE};

    public AssociationLabel(Type type, String ebXML) {
        super(type, ebXML);
    }

    @Override
    public String getJaxbValue() {
        return getEbXML30();
    }
}
