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
package org.openehealth.ipf.platform.manager.connection.ui.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.tree.ConnectionNode;

/**
 * Adapts a IConnection to a ConnectionNode. Registered in the platform.
 * 
 * @see IConnectionConfiguration
 * @see ConnectionNode
 * 
 * @author Mitko Kolev
 */
public class ConnectionAdapterFactory implements IAdapterFactory {

    @SuppressWarnings("unchecked")
    private static Class[] SUPPORTED_TYPES = new Class[] { ConnectionNode.class };

    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Object object, Class adapterType) {
        if (object.getClass().isAssignableFrom(ConnectionNode.class)) {
            ConnectionNode adapter = (ConnectionNode) object;

            return adapter.getAdapter(adapterType);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class[] getAdapterList() {
        return SUPPORTED_TYPES;
    }
}
