/*
 * Copyright 2013 the original author or authors.
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

import javax.xml.bind.annotation.XmlType;

/**
 * Document availability status code.
 */
@XmlType(name = "DocumentAvailability")
@EqualsAndHashCode(callSuper = true)
public class DocumentAvailability extends XdsEnum {
    private static final long serialVersionUID = -1839486987498308778L;

    /** Online indicates the Document in the Document Repository is available to be retrieved. */
    public static final DocumentAvailability OFFLINE = new DocumentAvailability(Type.OFFICIAL, "urn:ihe:iti:2010:DocumentAvailability:Offline");
    /** Offline indicates the Document in the Document Repository is not available to be retrieved. */
    public static final DocumentAvailability ONLINE = new DocumentAvailability(Type.OFFICIAL, "urn:ihe:iti:2010:DocumentAvailability:Online");

    public static final DocumentAvailability[] OFFICIAL_VALUES = {OFFLINE, ONLINE};

    public DocumentAvailability(Type type, String ebXML) {
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
