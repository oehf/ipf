/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import ca.uhn.hl7v2.model.Message;

/**
 * Interface for storages of HL7 v2 interactive continuation fragments.
 * @author Dmytro Rud
 */
public interface ContinuationStorage {
    
    /**
     * Puts a fragment into this storage.
     * 
     * @param continuationPointer
     *      continuation pointer of the fragment.  
     *      <code>null</code> values must be allowed.
     * @param queryTag
     *      query tag of the fragment (QPD-2).
     * @param msh31
     *      MSH-3-1 of the request.
     * @param msh32
     *      MSH-3-2 of the request.
     * @param msh33
     *      MSH-3-3 of the request.
     * @param fragment
     *      fragment as a HAPI message instance. 
     */
    void putFragment(
            String continuationPointer, 
            String queryTag, 
            String msh31, 
            String msh32,
            String msh33, 
            Message fragment);
    
    
    /**
     * Retrieves a fragment from the storage or <code>null</code> 
     * when no fragment with the given parameters could be found.
     * 
     * @param continuationPointer
     *      continuation pointer of the fragment.
     *      <code>null</code> values must be allowed.
     * @param queryTag
     *      query tag of the fragment (QPD-2).
     * @param msh31
     *      MSH-3-1 of the request.
     * @param msh32
     *      MSH-3-2 of the request.
     * @param msh33
     *      MSH-3-3 of the request.
     * @return 
     *      fragment as a HAPI message instance or <code>null</code> when none found.
     */
    Message getFragment(
            String continuationPointer, 
            String queryTag, 
            String msh31, 
            String msh32,
            String msh33);
    
    
    /**
     * Deletes all fragments which belong to the given query tag.
     *
     * @param queryTag
     *      query tag of the fragment (QPD-2).
     * @param msh31
     *      MSH-3-1 of the request.
     * @param msh32
     *      MSH-3-2 of the request.
     * @param msh33
     *      MSH-3-3 of the request.
     * @return
     *      <code>true</code>, when some fragments have been 
     *      actually deleted, i.e. when the given query tag is known.
     */
    boolean deleteFragments(
        String queryTag, 
        String msh31, 
        String msh32,
        String msh33);

}
