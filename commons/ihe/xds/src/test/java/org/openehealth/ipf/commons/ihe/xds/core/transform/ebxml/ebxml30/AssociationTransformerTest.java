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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.ebxml30;

import org.junit.jupiter.api.BeforeEach;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAssociation;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.AssociationTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.AssociationTransformerTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link AssociationTransformer}.
 * @author Jens Riemschneider.
 */
public class AssociationTransformerTest extends AssociationTransformerTestBase {
    @Override
    public EbXMLFactory createFactory() {
        return new EbXMLFactory30();
    }

    @Override
    @BeforeEach
    public void baseSetUp() {
        super.baseSetUp();
        association.setAvailabilityStatus(AvailabilityStatus.APPROVED);
    }

    @Override
    protected void checkExtraValues(EbXMLAssociation ebXML) {
        assertEquals(AvailabilityStatus.APPROVED, ebXML.getStatus());
    }
}
