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

import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * Note which supports initialization.
 * 
 * @author Mitko Kolev
 */
public abstract class InitializableNodeAbstract extends Node {

    private IConnectionConfiguration connectionConfiguration;

    public InitializableNodeAbstract(
            IConnectionConfiguration connectionConfiguration, String name) {
        super(name);
        this.connectionConfiguration = connectionConfiguration;
    }

    /**
     * @param name
     */
    public InitializableNodeAbstract(String name) {
        super(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if (adapter == IConnectionConfiguration.class) {
            return connectionConfiguration;
        } else
            return super.getAdapter(adapter);
    }

    /**
     * The method is called when the Node should initialize itself.
     * 
     * @throws IOException
     *             if the initialization fails.
     */
    public abstract void initialize() throws IOException;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        if (getClass() != obj.getClass())
            return false;
        final InitializableNodeAbstract other = (InitializableNodeAbstract) obj;
        if (connectionConfiguration == null) {
            if (other.connectionConfiguration != null)
                return false;
        } else if (!connectionConfiguration
                .equals(other.connectionConfiguration))
            return false;
        return true;
    }

}