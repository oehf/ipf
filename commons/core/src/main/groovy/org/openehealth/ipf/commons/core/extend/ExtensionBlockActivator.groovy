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
package org.openehealth.ipf.commons.core.extend

/**
 * @author Martin Krasser
 */
class ExtensionBlockActivator implements ConditionalActivator {

    boolean supports(Class<?> extensionClass) {
        getExtensionsProperty(extensionClass) != null
    }
     
    boolean supports(Object extensionObject) {
        getExtensionsProperty(extensionObject.class) != null
    }
     
    void activate(Class<?> extensionClass) {
        extensionClass.extensions.call()
    }
    
    void activate(Object extensionObject) {
        extensionObject.extensions.call()
    }
   
    private def getExtensionsProperty(Class<?> clazz) {
        clazz.metaClass.getMetaProperty('extensions')
    }
     
}
