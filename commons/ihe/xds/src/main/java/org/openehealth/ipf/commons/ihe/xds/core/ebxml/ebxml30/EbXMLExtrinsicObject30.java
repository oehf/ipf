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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ExtrinsicObjectType;

import javax.activation.DataHandler;

/**
 * Encapsulation of {@link ExtrinsicObjectType}.
 * @author Jens Riemschneider
 */
public class EbXMLExtrinsicObject30 extends EbXMLRegistryObject30<ExtrinsicObjectType> implements EbXMLExtrinsicObject {
    /**
     * Constructs an extrinsic object by wrapping an ebXML 3.0 object.
     * @param extrinsic
     *          the object to wrap.
     * @param objectLibrary
     *          the object library to use.
     */
    public EbXMLExtrinsicObject30(ExtrinsicObjectType extrinsic, EbXMLObjectLibrary objectLibrary) {
        super(extrinsic, objectLibrary);
    }
    
    @Override
    public String getMimeType() {
        return getInternal().getMimeType();
    }

    @Override
    public void setMimeType(String mimeType) {
        getInternal().setMimeType(mimeType);
    }

    @Override
    public AvailabilityStatus getStatus() {
        return AvailabilityStatus.valueOfOpcode(getInternal().getStatus());
    }

    @Override
    public void setStatus(AvailabilityStatus status) {
        getInternal().setStatus(AvailabilityStatus.toQueryOpcode(status));
    }

    @Override
    public DataHandler getDataHandler() {
        return getInternal().getDataHandler();
    }

    @Override
    public void setDataHandler(DataHandler dataHandler) {
        getInternal().setDataHandler(dataHandler);
    }
}
