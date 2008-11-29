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
package org.openehealth.ipf.platform.manager.flowmanager.mock.internal;

import javax.management.NotificationListener;

import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerMBean;
import org.openehealth.ipf.platform.manager.flowmanager.impl.FlowManagerRepositorylImpl;

/**
 * 
 * @author Mitko Kolev
 */
public class FlowManagerRepositoryImplMock extends FlowManagerRepositorylImpl {

    /**
     * @param jMXConnectionManager
     */
    public FlowManagerRepositoryImplMock(
            IJMXConnectionManager jMXConnectionManager) {
        super(jMXConnectionManager);
    }

    @Override
    protected IFlowManagerMBean loadFlowManager(
            IConnectionConfiguration connectionConfiguration,
            NotificationListener listener) {
        return new FlowManagerMBeanMock();
    }

}
