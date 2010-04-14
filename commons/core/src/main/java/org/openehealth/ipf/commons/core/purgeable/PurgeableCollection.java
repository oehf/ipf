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
package org.openehealth.ipf.commons.core.purgeable;

/**
 * Basis class for special collection whose elements    
 * can be purged on the basis of their creation time. 
 * 
 * @author Dmytro Rud
 */
public interface PurgeableCollection {

    /**
     * Deletes all elements which have reached the given time-to-live.
     * @param ttl
     *      Time-to-live in milliseconds.
     */
    public void purge(long ttl);
}
