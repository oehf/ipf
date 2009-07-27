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
package org.openehealth.ipf.modules.cda.builder.support

import org.openhealthtools.ihe.common.cdar2.ANY

/**
 * @author Christian Ohr
 */
public class MetaBuilderUtils{

    /*
     * Checks that if nullFlavor is set all other properties are
     * null, and, vice versa, that if nullFlavor is NOT set that
     * at least the required property is set.
     */
    static boolean checkNullFlavor(ANY data, String requiredProp) {
        if (data) {
            if (data.nullFlavor) {
                // for all other properties...
                data.getMetaClass().getProperties().findAll { 
                    p -> p.name != 'nullFlavor' }
                .each { p ->
                    // ... either set to NULL ...
                    if (p.setter) {
                        p.setProperty(data, null)
                    // ... or clean the collection
                    } else {
                        def prop = p.getProperty(data)
                        if (prop instanceof Collection) {
                            prop.clear()
                        }
                    }
                }
            } else {
                // Check if required property is set
                def p = data.metaClass.getProperty(data, requiredProp)
                if (p != null) {
                    if (p instanceof Collection) {
                        return !p.isEmpty()
                    }
                } else {
                    return false
                }
            }
        }
        return true
    }
    
}
