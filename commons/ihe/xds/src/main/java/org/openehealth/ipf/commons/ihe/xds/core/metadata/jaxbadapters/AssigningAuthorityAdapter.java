/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * A JAXB {@link XmlAdapter} that creates a simplified representation of an
 * {@link AssigningAuthority} which just contains the universal id without the
 * namespace id and the universal id type. In XDS these fields are not used
 * and they get in the way when trying to serialize in a simple HL7 V3 format.
 */
public class AssigningAuthorityAdapter extends XmlAdapter<String, AssigningAuthority> {
    @Override
    public String marshal(AssigningAuthority v) throws Exception {
        return (v != null) ? v.getUniversalId() : null;
    }

    @Override
    public AssigningAuthority unmarshal(String v) throws Exception {
        return new AssigningAuthority(v);
    }
}
