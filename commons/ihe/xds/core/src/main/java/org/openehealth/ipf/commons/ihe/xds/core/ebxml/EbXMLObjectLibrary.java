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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A container of objects used for resolving object references and their ids.
 * @author Jens Riemschneider
 */
public class EbXMLObjectLibrary {
    private final Map<String, Object> objLib = new HashMap<String, Object>();
    private final Map<Object, String> reverseLib = new HashMap<Object, String>();
    
    /**
     * Puts an object into the library.
     * <p>
     * The object will only be added if both parameters are non-<code>null</code>.
     * @param id
     *          the id of the object. Can be <code>null</code>.
     * @param obj
     *          the object. Can be <code>null</code>.
     */
    public void put(String id, Object obj) {
        if (id != null && obj != null) {
            objLib.put(id, obj);
            reverseLib.put(obj, id);
        }
    }
    
    /**
     * Looks up an object via its ID.
     * @param id
     *          the id. Can be <code>null</code>.
     * @return the object or <code>null</code> if the input was <code>null</code>
     *          of if the library does not contain an object with the given id.
     */
    public Object getById(String id) {
        if (id == null) {
            return null;
        }
        return objLib.get(id);
    }
    
    /**
     * Looks up an ID via its object. 
     * @param obj
     *          the object. Can be <code>null</code>.
     * @return the ID or <code>null</code> if the input was <code>null</code>
     *          or if the library does not contain the object.
     */
    public String getByObj(Object obj) {
        if (obj == null) {
            return null;
        }
        return reverseLib.get(obj);
    }
    
    /**
     * @return a read-only collection of all objects in the library. 
     */
    public Collection<Object> getObjects() {
        return Collections.unmodifiableCollection(objLib.values());
    }
}
