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
package org.openehealth.ipf.modules.hl7dsl.util

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.DeepCopy;
import ca.uhn.hl7v2.util.Terser;

/**
 * @author Martin Krasser
 */
class MessageCopy {

    Message src
    Message dst
    
    Terser srcTerser
    Terser dstTerser

    MessageCopy(Message src, Message dst) {
        this.src = src
        this.dst = dst
        this.srcTerser = new Terser(src)
        this.dstTerser = new Terser(dst)
    }
	
	// TODO refactor to use GroupAdapterIterator
    void execute() {
        doExecute('/', src)
    }
    
    private void doExecute(String path, Group grp) {
        grp.names.each { name ->
            grp.getAll(name).eachWithIndex { structure, index ->
                String spec = spec(path, structure, index)
                if (structure instanceof Group) {
                    doExecute(spec, structure) // recursion
                } else {
                    copySegment(spec)
                }
            }
        }
    }
    
    private copySegment(spec) {
        try {
            DeepCopy.copy(
                    srcTerser.getSegment(spec), 
                    dstTerser.getSegment(spec))
        } catch (HL7Exception e) {
            throw new RuntimeException("Either source or destination segment does not exist", e)
        }
    }
    
    private static String spec(def path, def str, def idx) {
        path + (path == '/' ? '' : '/') + str.name + '(' + idx + ')'
    }
    
}
