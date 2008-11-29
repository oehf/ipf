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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterFactory;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;

/**
 * Not registered in the plugin.xml!
 * 
 * Supports {@link IFlowInfo}
 * 
 * @author Mitko Kolev
 */
public class FlowManagerAdapterFactory implements IAdapterFactory {

    @SuppressWarnings("unchecked")
    private static Class[] SUPPORTED_TYPES = new Class[] { IFlowInfo.class };

    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Object object, Class adapterType) {
        if (object.getClass().equals(FlowInfoAdapter.class)) {
            FlowInfoAdapter adapter = (FlowInfoAdapter) object;
            return adapter.getFlowInfo();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class[] getAdapterList() {
        return SUPPORTED_TYPES;
    }

    public static List<FlowInfoAdapter> createFlowInfoAdapters(
            IConnectionConfiguration connectionConfiguration,
            List<IFlowInfo> flowInfos) {
        List<FlowInfoAdapter> adapters = new ArrayList<FlowInfoAdapter>();
        for (IFlowInfo flow : flowInfos) {
            adapters.add(new FlowInfoAdapter(connectionConfiguration, flow));
        }
        return adapters;
    }
}
