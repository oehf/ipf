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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.tree.InitializableNodeAbstract;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.builder.ConnectionNodeBuilder;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.builder.ConnectionNodeBuilderFactory;

/**
 * Represents the root node element for a JMX Connection
 * 
 * @see InitializableNodeAbstract
 * @see Node
 * @see IConnectionConfiguration
 * 
 * @author Mitko Kolev
 */
public class JMXConnectionNode extends InitializableNodeAbstract {

    private boolean isInitialized;

    private final static Log log = LogFactory.getLog(JMXConnectionNode.class);

    /**
     * @param connectionConfiguration
     * @param name
     */
    public JMXConnectionNode(IConnectionConfiguration connectionConfiguration,
            String name) {
        super(connectionConfiguration, name);
        isInitialized = false;
    }

    @Override
    public void initialize() throws IOException {
        ConnectionNodeBuilder builder = ConnectionNodeBuilderFactory
                .getBuilder();
        try {
            builder.createConnectionNode(this);
            isInitialized = true;
        } catch (IOException e) {
            log.debug("Cannot initialize the JMX Tree");

        }
    }

    /**
     * Returns if the node has ever been initialized.
     * 
     * @return
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
