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
package org.openehealth.ipf.commons.ihe.xds.core.transform.responses.ebxml21;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLFactory21;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.QueryResponseTransformerTestBase;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.QueryResponseTransformer;

/**
 * Tests for {@link QueryResponseTransformer}.
 * @author Jens Riemschneider
 */
public class QueryResponseTransformerTest extends QueryResponseTransformerTestBase {

    @Override
    public EbXMLFactory createFactory() {
        return new EbXMLFactory21();
    }
}
