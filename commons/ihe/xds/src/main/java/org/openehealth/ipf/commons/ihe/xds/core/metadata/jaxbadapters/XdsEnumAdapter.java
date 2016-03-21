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
package org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.XdsEnum;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XdsEnumFactory;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Dmytro Rud
 */
abstract public class XdsEnumAdapter<T extends XdsEnum> extends XmlAdapter<XdsEnumJaxbElement, T> {

    private final XdsEnumFactory<T> factory;

    public XdsEnumAdapter(XdsEnumFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public T unmarshal(XdsEnumJaxbElement jaxb) throws Exception {
        if (jaxb == null) {
            return null;
        }

        if (jaxb.getType() == XdsEnum.Type.OFFICIAL) {
            for (T xds : factory.getOfficialValues()) {
                if (xds.getJaxbValue().equals(jaxb.getValue())) {
                    return xds;
                }
            }
        }

        return factory.fromEbXML(jaxb.getValue());
    }

    @Override
    public XdsEnumJaxbElement marshal(T xds) throws Exception {
        if (xds == null) {
            return null;
        }

        XdsEnumJaxbElement jaxb = new XdsEnumJaxbElement();
        jaxb.setType(xds.getType());
        jaxb.setValue(xds.getJaxbValue());
        return jaxb;
    }
}
