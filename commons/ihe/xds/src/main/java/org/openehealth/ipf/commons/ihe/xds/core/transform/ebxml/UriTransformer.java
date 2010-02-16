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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml;

import java.util.ArrayList;
import java.util.List;

/**
 * Transforms between a URI String to its corresponding slot value list. 
 * @author Jens Riemschneider
 */
public class UriTransformer {
    /**
     * Transforms the slot values into a URI string.
     * @param slotValues
     *          the slot values.
     * @return the URI string. <code>null</code> if the result would be an empty string.
     */
    public String fromEbXML(List<String> slotValues) {
        if (slotValues.size() == 1) {
            String slotValue = slotValues.get(0);
            if (slotValue.indexOf('|') == -1) {
                return slotValue;
            }
        }
        
        String[] uriParts = new String[10];
        for (String slotValue : slotValues) {
            int separatorIdx = slotValue.indexOf('|');
            if (separatorIdx > 0) {
                int uriIdx = Integer.parseInt(slotValue.substring(0, separatorIdx));
                if (uriIdx < 10) {
                    uriParts[uriIdx] = slotValue.substring(separatorIdx + 1);
                }
            }
        }
        
        StringBuilder builder = new StringBuilder();
        for (String uriPart : uriParts) {
            if (uriPart != null) {
                builder.append(uriPart);
            }
        }
        
        String uri = builder.toString();
        return uri.isEmpty() ? null : uri;
    }

    /**
     * Transforms the URI into the slot values.
     * @param uri
     *          the URI string. Can be <code>null</code>.
     * @return the slot values. Never <code>null</code>.
     */
    public String[] toEbXML(String uri) {
        if (uri == null) {
            return new String[0];
        }
        
        List<String> uriParts = new ArrayList<String>();
        
        int slotIdx = 1;
        int start = 0;
        while (start < uri.length()) {
            String prefix = slotIdx + "|";
            int validLength = 128 - prefix.length();
            if (uri.length() < start + validLength) {
                validLength = uri.length() - start;
            }
            
            String uriPart = uri.substring(start, start + validLength);
            String slotValue = prefix + uriPart;
            uriParts.add(slotValue);
            
            start += validLength;
            ++slotIdx;
        }
        
        return uriParts.toArray(new String[uriParts.size()]);
    }
}
