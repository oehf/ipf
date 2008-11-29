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
package org.openehealth.ipf.commons.flow.task;

import org.openehealth.ipf.commons.flow.FlowManager;
import org.openehealth.ipf.commons.flow.ManagedMessage;

/**
 * @author Martin Krasser
 */
public abstract class FlowTaskSupport extends Thread {

    private FlowManager flowManager;
    
    private ManagedMessage message;
    
    private int numLoops;
    
    public ManagedMessage getMessage() {
        return message;
    }

    public void setMessage(ManagedMessage message) {
        this.message = message;
    }

    public FlowManager getFlowManager() {
        return flowManager;
    }

    public void setFlowManager(FlowManager flowManager) {
        this.flowManager = flowManager;
    }

    public int getNumLoops() {
        return numLoops;
    }

    public void setNumLoops(int numLoops) {
        this.numLoops = numLoops;
    }

    @Override
    public void run() {
        for (int i = 0; i < getNumLoops(); i++) {
            doRun();
        }
    }
    
    protected abstract void doRun();

}
