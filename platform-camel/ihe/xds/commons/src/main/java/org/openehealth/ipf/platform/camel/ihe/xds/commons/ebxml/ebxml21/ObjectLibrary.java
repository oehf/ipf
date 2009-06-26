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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A container of objects used for resolving object references and their ids.
 * @author Jens Riemschneider
 */
public class ObjectLibrary {
    private final Map<String, Object> objLib = new HashMap<String, Object>();
    private final Map<Object, String> reverseLib = new HashMap<Object, String>();
    
    public void put(String id, Object obj) {
        objLib.put(id, obj);
        reverseLib.put(obj, id);
    }
    
    public Object getById(String id) {
        if (id == null) {
            return null;
        }
        return objLib.get(id);
    }
    
    public String getByObj(Object obj) {
        if (obj == null) {
            return null;
        }
        return reverseLib.get(obj);
    }
    
    public Collection<Object> getObjects() {
        return Collections.unmodifiableCollection(objLib.values());
    }
}
