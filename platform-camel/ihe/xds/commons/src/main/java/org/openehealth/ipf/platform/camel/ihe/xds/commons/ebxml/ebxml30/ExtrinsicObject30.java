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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExtrinsicObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ExtrinsicObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory;

/**
 * Encapsulation of {@link ExtrinsicObjectType}.
 * @author Jens Riemschneider
 */
public class ExtrinsicObject30 extends RegistryEntry30<ExtrinsicObjectType> implements ExtrinsicObject {
    private final static ObjectFactory rimFactory = new ObjectFactory();

    private ExtrinsicObject30(ExtrinsicObjectType extrinsic) {
        super(extrinsic);
    }
    
    // TODO: Should be package private once the higher level objects are implemented
    public static ExtrinsicObject30 create(ExtrinsicObjectType extrinsicObject) {
        return new ExtrinsicObject30(extrinsicObject);
    }

    static ExtrinsicObject30 create(String id) {
        ExtrinsicObjectType extrinsicObjectType = rimFactory.createExtrinsicObjectType();
        extrinsicObjectType.setId(id);
        return new ExtrinsicObject30(extrinsicObjectType);
    }

    @Override
    public String getMimeType() {
        return getInternal().getMimeType();
    }

    @Override
    public void setMimeType(String mimeType) {
        getInternal().setMimeType(mimeType);
    }
}
