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
package org.openehealth.ipf.commons.ihe.xds.core.validate;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Validates a value list for compliance with a URI (RFC 2616).
 * @author Jens Riemschneider
 */
public class UriValidator implements ValueListValidator {
    private static final Pattern PATTERN = Pattern.compile("([1-9])\\|(.*)");

    @Override
    public void validate(List<String> values) throws XDSMetaDataException {
        if (values.size() > 0) {
            String uri = getUri(values);
            metaDataAssert(uri != null, NULL_URI);
            metaDataAssert(!uri.isEmpty(), EMPTY_URI);
            
            // Accept anything that the classes URI or URL accept. This is done to
            // avoid e.g. "http://" to fail. The XDSToolKit is using this URI
            // for some tests and the RFCs do not clearly state if this is a valid
            // URI or not. The URL class seems to accept it, the URI class doesn't.
            try {
                new URI(uri);
            }
            catch (URISyntaxException eUri) {
                try {
                    new URL(uri);
                } catch (MalformedURLException eUrl) {
                    throw new XDSMetaDataException(INVALID_URI, uri);
                }
            }
        }
    }
    
    private String getUri(List<String> values) throws XDSMetaDataException {
        if (values.size() == 1) {
            String uri = values.get(0);
            if (uri == null) {
                return null;
            }
            
            if (uri.indexOf('|') == -1) {
                return uri;
            }
        }
        
        String[] uriParts = new String[10];
        int highestIdx = 0;
        for (String slotValue : values) {
            metaDataAssert(slotValue != null, NULL_URI_PART);
            
            Matcher matcher = PATTERN.matcher(slotValue);
            metaDataAssert(matcher.matches(), INVALID_URI_PART, slotValue);
            
            int uriIdx = Integer.parseInt(matcher.group(1));
            uriParts[uriIdx] = matcher.group(2);
            highestIdx = Math.max(highestIdx, uriIdx);
        }
        
        StringBuilder builder = new StringBuilder();
        for (int idx = 1; idx <= highestIdx; ++idx) {
            String uriPart = uriParts[idx];
            metaDataAssert(uriPart != null, MISSING_URI_PART, idx);
            builder.append(uriPart);
        }
        
        return builder.toString();
    }
}
