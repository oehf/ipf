/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.validation

import ca.uhn.hl7v2.conf.store.ProfileStore


/**
 * Store implementation for Confirmance Profile files that looks in
 * the classpath.
 * 
 * @author Christian Ohr
 */
public class ClassPathProfileStore implements ProfileStore{
    
    String offset = ""
    
    /**
     * @see ca.uhn.hl7v2.conf.store.ProfileStore#getProfile(java.lang.String)
     */
    public String getProfile(String ID){
        this.class.getResourceAsStream("/${offset}${ID}.xml")?.getText()
    }
    
    /**
     * @see ca.uhn.hl7v2.conf.store.ProfileStore#persistProfile(java.lang.String, java.lang.String)
     */
    public void persistProfile(String arg0, String arg1){
        throw new UnsupportedOperationException("Not implemented yet")
    }
    
}
