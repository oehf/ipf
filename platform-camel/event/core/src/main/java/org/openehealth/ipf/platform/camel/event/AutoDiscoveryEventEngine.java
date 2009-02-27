/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.event;

import java.util.Map;

import org.openehealth.ipf.commons.event.EventEngine;
import org.openehealth.ipf.commons.event.EventObject;
import org.openehealth.ipf.commons.event.Subscription;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Event engine implementation that performs automatic discovery of subscriptions
 * @author Jens Riemschneider
 */
public class AutoDiscoveryEventEngine extends EventEngine implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map subscriptions = applicationContext.getBeansOfType(Subscription.class);
        for (Object sub : subscriptions.values()) {
            subscribe((Subscription) sub);
        }
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.event.EventEngine#distributeToHandlers(org.openehealth.ipf.commons.event.EventObject)
     */
    @Override
    public void distributeToHandlers(EventObject event) {
        // TODO Auto-generated method stub
        super.distributeToHandlers(event);
    }
}
