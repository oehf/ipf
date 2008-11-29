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
package org.openehealth.ipf.platform.manager.connection.ui.tree;

import java.io.IOException;
import java.util.List;

import org.eclipse.ui.IViewSite;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.connection.ui.osgi.Activator;

/**
 * JMX Content provider for the Connection nodes.
 * 
 * @see org.eclipse.jface.viewers.ITreeContentProvider
 * 
 * @author Mitko Kolev
 */
public class JMXContentProvider extends ConnectionNodeContentProviderAbstract {

    private ConnectionTree tree;

    public JMXContentProvider(IViewSite owner,
            IJMXConnectionManager jMXConnectionManager) {
        super(owner);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.openehealth.ipf.platform.manager.connection.ui.tree.
     * ConnectionNodeContentProviderAbstract#initializeTree()
     */
    @Override
    public void initialize() throws IOException {
        tree = new ConnectionTree();
        IJMXConnectionManager jMXConnectionManager = Activator.getDefault()
                .getJMXConnectionManager();

        List<IConnectionConfiguration> connectionConfigurations = jMXConnectionManager
                .getConnectionConfigurations();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            ConnectionNode connectionNode = new ConnectionNode(
                    connectionConfiguration, connectionConfiguration.getName());
            // fill the MBeans in the tree
            connectionNode.initialize();
            tree.addChild(connectionNode);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.openehealth.ipf.platform.manager.connection.ui.tree.
     * ConnectionNodeContentProviderAbstract#getTree()
     */
    @Override
    public ConnectionTree getTree() {
        return tree;
    }
}
