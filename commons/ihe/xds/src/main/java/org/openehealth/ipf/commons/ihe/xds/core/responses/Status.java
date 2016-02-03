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
package org.openehealth.ipf.commons.ihe.xds.core.responses;

import groovy.transform.EqualsAndHashCode;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XdsVersionedEnum;

/**
 * Status information according to the XDS specification.
 * @author Jens Riemschneider
 */
@EqualsAndHashCode(callSuper = true)
public class Status extends XdsVersionedEnum {
    private static final long serialVersionUID = 2594465927152837635L;

    /** The request execution failed. */
    public static final Status FAILURE = new Status(Type.OFFICIAL, "Failure", "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
    /** The request execution succeeded. */
    public static final Status SUCCESS = new Status(Type.OFFICIAL, "Success", "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success");
    /** The request execution partially succeeded. */
    public static final Status PARTIAL_SUCCESS = new Status(Type.OFFICIAL, "PartialSuccess", "urn:ihe:iti:2007:ResponseStatusType:PartialSuccess");

    public static final Status[] OFFICIAL_VALUES = {FAILURE, SUCCESS, PARTIAL_SUCCESS};

    public Status(Type type, String ebXML21, String ebXML30) {
        super(type, ebXML21, ebXML30);
    }

    @Override
    public String getJaxbValue() {
        return getEbXML21();
    }
}

