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

import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.properties.JMXConnectionPropertiesSource;

/**
 * <p>
 * The top element which visualizes a IConnection object in the views.
 * 
 * @see org.openehealth.ipf.platform.manager.connection.ui.tree.Node
 * @author Mitko Kolev
 */
public class ConnectionNode extends InitializableNodeAbstract {

    /**
     * @param name
     */
    private final IPropertySource2 propertiesSource;

    /**
     * Creates a property source for the given connection.
     * 
     * @param connectionConfiguration
     *            the connection, this node has to visualize.
     * @param name
     *            the name of the Node.
     */
    public ConnectionNode(IConnectionConfiguration connectionConfiguration,
            String name) {
        super(connectionConfiguration, name);
        this.propertiesSource = new JMXConnectionPropertiesSource(
                connectionConfiguration);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if (adapter == IPropertySource.class
                || adapter == IPropertySource2.class) {
            return propertiesSource;
        } else
            return super.getAdapter(adapter);

    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.openehealth.ipf.platform.manager.connection.ui.tree.
     * InitializableNodeAbstract#initialize()
     */
    @Override
    public void initialize() throws IOException {

    }

}
