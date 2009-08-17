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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Martin Krasser
 */
public class FlowInsertTask extends FlowTaskSupport {

    private static final Log LOG = LogFactory.getLog(FlowInsertTask.class);
    
    @Override
    protected void doRun() {
        long id = getFlowManager().beginFlow(getMessage(), "test");
        getMessage().setFlowId(null); // do not pretend replay
        LOG.info("generated id = " + id + "(" + Thread.currentThread() + ")");
    }
}
