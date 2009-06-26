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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RegistryPackage;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryPackageType;

/**
 * Encapsulation for {@link RegistryPackageType}.
 * @author Jens Riemschneider
 */
public class RegistryPackage21 extends RegistryEntry21<RegistryPackageType> implements RegistryPackage {
    private final static ObjectFactory rimFactory = new ObjectFactory();

    private RegistryPackage21(RegistryPackageType registryPackage, ObjectLibrary objectLibrary) {
        super(registryPackage, objectLibrary);
    }
    
    static RegistryPackage21 create(ObjectLibrary objectLibrary, String id) {
        RegistryPackageType registryPackageType = rimFactory.createRegistryPackageType();
        registryPackageType.setId(id);
        return new RegistryPackage21(registryPackageType, objectLibrary);
    }
}
