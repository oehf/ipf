/**
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
package org.openehealth.ipf.modules.cda.builder;

import groovy.lang.Closure;
import groovytools.builder.CreateNodeEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Example of a MetaBuilder Visitor object that is called on every 
 * node creation.
 * 
 * @author Christian Ohr
 */
public class LoggingCallback extends Closure {

    private static final long serialVersionUID = 4962003375401288943L;
    private static Log LOG = LogFactory.getLog(LoggingCallback.class);
    private int nodesCreated;

    public LoggingCallback() {
        super(null);
    }
    
    public Object call(Object e) {
        CreateNodeEvent cne = (CreateNodeEvent)e;
        nodesCreated++;
        //if (LOG.isDebugEnabled()) {
            if(cne.getIsRoot()) {
                System.out.println("Created root node \"" + cne.getName() + "\" of type " + cne.getNode().getClass().getSimpleName());
            } else {
                System.out.println("Created node \"" + cne.getName() + "\" of type " + cne.getNode().getClass().getSimpleName());
            }
        //}
        // Must return the node
        return cne.getNode();
    }

    public int getNodesCreated() {
        return nodesCreated;
    }

}
