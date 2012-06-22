/*
 * Copyright 2012 the original author or authors.
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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Name;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XpnName;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * A JAXB {@link XmlAdapter} that allows the Name class to be serialized.
 * <p/>
 * The original implementation of the {@link Name} class was concrete and had
 * no subclasses. The XML serialization of a {@link Name} used to look like this:
 * <pre>
 * {@code
 *
 * <name>
 *    <given>John</given>
 *    <family>Doe</family>
 * </name>
 * }
 * </pre>
 * <p/>
 * When the {@link Name} class was made abstract, the XML above could no
 * longer be serialized. This adapter allows the XML shown above to be
 * serialized without change.
 * @author Michael Ottati
 */
public class NameAdapter extends XmlAdapter<XpnName,Name> {
    @Override
    public Name unmarshal(XpnName v) throws Exception {
        return v;
    }

    @Override
    public XpnName marshal(Name v) throws Exception {

        return new XpnName( v.getFamilyName(),
                            v.getGivenName(),
                            v.getSecondAndFurtherGivenNames(),
                            v.getSuffix(),
                            v.getPrefix(),
                            v.getDegree()
        );
    }
}
