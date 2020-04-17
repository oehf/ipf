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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * A JAXB {@link XmlAdapter} that creates a simplified representation of
 * a {@link LocalizedString} which just contains the string itself without
 * the language and charset information. There is at least one case in the
 * HL7 V3 serialization in which this simplified representation is needed.
 */
public class LocalizedStringAdapter extends XmlAdapter<String, LocalizedString> {
    @Override
    public String marshal(LocalizedString v) throws Exception {
        return (v != null) ? v.getValue() : null;
    }

    @Override
    public LocalizedString unmarshal(String v) throws Exception {
        return new LocalizedString(v);
    }
}
