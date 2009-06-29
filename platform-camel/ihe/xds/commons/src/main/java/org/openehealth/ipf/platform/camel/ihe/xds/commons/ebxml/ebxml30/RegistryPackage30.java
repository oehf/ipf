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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RegistryPackage;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.RegistryPackageType;

/**
 * Encapsulation for {@link RegistryPackageType}.
 * @author Jens Riemschneider
 */
public class RegistryPackage30 extends RegistryEntry30<RegistryPackageType> implements RegistryPackage {
    private final static ObjectFactory rimFactory = new ObjectFactory();

    private RegistryPackage30(RegistryPackageType registryPackage) {
        super(registryPackage);
    }
    
    static RegistryPackage30 create(String id) {
        RegistryPackageType registryPackageType = rimFactory.createRegistryPackageType();
        registryPackageType.setId(id);
        return new RegistryPackage30(registryPackageType);
    }

    static RegistryPackage30 create(RegistryPackageType registryPackage) {
        return new RegistryPackage30(registryPackage);
    }
}
