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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates a value list for compliance with a URI (RFC 2616).
 * @author Jens Riemschneider
 */
public class UriValidator implements ValueValidator {

    @Override
    public void validate(String uri) throws XDSMetaDataException {
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
