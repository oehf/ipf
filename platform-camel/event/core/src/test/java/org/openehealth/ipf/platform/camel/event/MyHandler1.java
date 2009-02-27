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

import org.openehealth.ipf.commons.event.EventHandler;
import org.openehealth.ipf.commons.event.EventObject;

public class MyHandler1 implements EventHandler {
    private boolean handled;
    private String prop;
    private MyEventImpl1 handledEvent;

    public void handle(EventObject eventObject) {
        handled = true;
        if (eventObject instanceof MyEventImpl1){
            handledEvent = (MyEventImpl1) eventObject;
            prop = handledEvent.getProp();
        }
    }
    
    public boolean isHandled() {
        return handled;
    }

    public String getProp() {
        return prop;
    }
    
    public MyEventImpl1 getHandledEvent() {
        return handledEvent;
    }
    
    public void reset() {
        handled = false;
    }
}