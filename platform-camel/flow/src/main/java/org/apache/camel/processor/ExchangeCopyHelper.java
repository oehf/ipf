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
package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.commons.flow.history.SplitHistory;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * For package-protected access to {@link MulticastProcessor}.
 * 
 * @author Martin Krasser
 */
public class ExchangeCopyHelper {

    public static void afterCopy(Exchange exchange, Iterable<?> pairs) {
        ArrayList<ManagedMessage> copies = new ArrayList<>(countElements(pairs));
        for (Object p : pairs) {
            ProcessorExchangePair pair = (ProcessorExchangePair) p;
            copies.add(new PlatformMessage(pair.getExchange()));
        }
        afterCopy(new PlatformMessage(exchange), copies);
    }
    
    private static void afterCopy(ManagedMessage message, List<ManagedMessage> copies) {
        if (copies.size() < 2) {
            return;
        }
        SplitHistory history = message.getSplitHistory();
        SplitHistory[] splits = history.split(copies.size());
        
        for (int i = 0; i < splits.length; i++) {
            copies.get(i).setSplitHistory(splits[i]);
        }
        
    }
    
    private static int countElements(Iterable<?> pairs) {
        // Currently we can only handle split results
        // that are lists because we need its size.
        if (pairs instanceof List) {
             return ((List<?>)pairs).size();
        } else {
            throw new IllegalArgumentException("Cannot determine split size from split result.");
        }
    }
    
}
