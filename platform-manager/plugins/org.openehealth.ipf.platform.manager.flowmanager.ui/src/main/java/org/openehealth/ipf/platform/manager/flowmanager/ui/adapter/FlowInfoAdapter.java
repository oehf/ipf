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
package org.openehealth.ipf.platform.manager.flowmanager.ui.adapter;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertySource;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.ui.properties.FlowPropertiesSource;

/**
 * Adapts a connection to:
 * {@link org.eclipse.ui.views.properties.IPropertySource} and {@link IFlowInfo}
 * <p>
 * 
 * @see org.eclipse.ui.views.properties.IPropertySource
 * @see org.eclipse.core.runtime.IAdaptable
 * @see org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo
 * 
 * @author Mitko Kolev
 */
public class FlowInfoAdapter implements IAdaptable {

    private final IFlowInfo flowInfo;

    private final FlowPropertiesSource propertySource;

    private final IConnectionConfiguration connectionConfiguration;

    /**
     * Creates an adapter of the connection and its associated flowInfo adaptee.
     * 
     * @param connectionConfiguration
     * @param flowInfo
     */
    public FlowInfoAdapter(IConnectionConfiguration connectionConfiguration,
            IFlowInfo flowInfo) {
        this.flowInfo = flowInfo;
        this.connectionConfiguration = connectionConfiguration;
        propertySource = new FlowPropertiesSource(flowInfo);
    }

    /**
     * Returns the IFlowInfo associated with the connection.
     * 
     * @return
     */
    public IFlowInfo getFlowInfo() {
        return flowInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if (adapter == IPropertySource.class) {
            return propertySource;
        } else if (adapter == IFlowInfo.class) {
            return flowInfo;
        } else if (adapter == IConnectionConfiguration.class) {
            return connectionConfiguration;
        }
        return null;
    }
}
