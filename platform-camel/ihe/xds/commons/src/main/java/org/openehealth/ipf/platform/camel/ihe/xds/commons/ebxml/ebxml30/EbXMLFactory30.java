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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAssociation;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExtrinsicObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RegistryPackage;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.SubmitObjectsRequest;

/**
 * Factory for EbXML 2.1 objects.
 * @author Jens Riemschneider
 */
public class EbXMLFactory30 implements EbXMLFactory {
    @Override
    public Classification createClassification(ObjectLibrary objectLibrary) {
        return Classification30.create();
    }

    @Override
    public ExtrinsicObject createExtrinsic(String id, ObjectLibrary objectLibrary) {
        return ExtrinsicObject30.create(id);
    }

    @Override
    public RegistryPackage createRegistryPackage(String id, ObjectLibrary objectLibrary) {
        return RegistryPackage30.create(id);
    }

    @Override
    public EbXMLAssociation createAssociation(ObjectLibrary objectLibrary) {
        return EbXMLAssociation30.create();
    }
    
    @Override
    public SubmitObjectsRequest createSubmitObjectsRequest(ObjectLibrary objectLibrary) {
        return SubmitObjectsRequest30.create();
    }

    @Override
    public ObjectLibrary createObjectLibrary() {
        return new ObjectLibrary();
    }
}
