/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * Custom interceptor for testing custom interceptors.
 * @author Dmytro Rud
 */
public class CustomInterceptor extends AbstractPhaseInterceptor<Message> {
    private static final Map<String, AtomicInteger> map = new HashMap<>();
    
    public CustomInterceptor(String id) {
        super(id, Phase.PRE_PROTOCOL);
        map.put(id, new AtomicInteger(0));
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        map.get(getId()).incrementAndGet();
    }

    public static int getAt(String key) {
        return map.get(key).get();
    }
    
    public static void clear() {
        map.clear();
    }
}
