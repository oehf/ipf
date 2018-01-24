/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.modules.hl7dsl
/**
 * @author Mitko Kolev
 * @deprecated the ipd-modules-hl7dsl module is deprecated
 */
public interface AbstractAdapter<T> {
    
    /**
     * @return <code>true</code> if the {@link #getTarget()} element is empty
     */
    boolean isEmpty()
    
   /**
    * @return the adapter target. This should be a HAPI object
    */
    T getTarget()
    
    /**
    * @return the position of the element target in the message
    */
    String getPath()
   
    /**
    * @return sets the position of the element target in the message
    */
    void setPath(String path)

}
