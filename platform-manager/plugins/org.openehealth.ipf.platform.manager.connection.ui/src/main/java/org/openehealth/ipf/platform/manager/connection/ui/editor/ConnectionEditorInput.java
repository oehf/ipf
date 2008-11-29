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
package org.openehealth.ipf.platform.manager.connection.ui.editor;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * The default editor for IConnectionConfigurations is registered with extension
 * .connection. Objects of this class can be put in a hash table.
 * 
 * @author Mitko Kolev
 */
public class ConnectionEditorInput implements IEditorInput {

    /**
     * The default editor for connections must be registered with this
     * extenision.
     */
    public static final String CONNECTION_EDITOR_FILE_EXTENSION_ID = "connection";

    static class ConnectionAdaptable implements IAdaptable {

        private final IConnectionConfiguration connectionConfiguration;

        public ConnectionAdaptable(
                IConnectionConfiguration connectionConfiguration) {
            this.connectionConfiguration = connectionConfiguration;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object getAdapter(Class adapter) {
            if (adapter == IConnectionConfiguration.class) {
                return connectionConfiguration;
            }
            return null;

        }
    }

    /*
     * Could be also null
     */
    protected final IAdaptable adapter;

    public ConnectionEditorInput(
            IConnectionConfiguration connectionConfiguration) {
        this(new ConnectionAdaptable(connectionConfiguration));
    }

    /**
     * The adapter should be an adapter for a IConnection
     * 
     * @param adapter
     */
    public ConnectionEditorInput(IAdaptable adapter) {
        this.adapter = adapter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#exists()
     */
    @Override
    public boolean exists() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
     */
    @Override
    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getName()
     */
    @Override
    public String getName() {
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) adapter
                .getAdapter(IConnectionConfiguration.class);
        if (connectionConfiguration == null) {
            return "Connection Editor Overview";
        }
        return connectionConfiguration.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getPersistable()
     */
    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if (adapter.equals(IConnectionConfiguration.class)) {
            return this.adapter.getAdapter(IConnectionConfiguration.class);
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) this.adapter
                .getAdapter(IConnectionConfiguration.class);
        result = prime
                * result
                + ((connectionConfiguration == null) ? 0
                        : connectionConfiguration.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof ConnectionEditorInput) {
            final ConnectionEditorInput other = (ConnectionEditorInput) obj;
            IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) this.adapter
                    .getAdapter(IConnectionConfiguration.class);

            IConnectionConfiguration otherConnectionConfiguration = (IConnectionConfiguration) other.adapter
                    .getAdapter(IConnectionConfiguration.class);
            if (connectionConfiguration == null) {
                if (otherConnectionConfiguration != null)
                    return false;
            } else if (!connectionConfiguration
                    .equals(otherConnectionConfiguration))
                return false;
        } else {
            return false;
        }
        return true;
    }

}
