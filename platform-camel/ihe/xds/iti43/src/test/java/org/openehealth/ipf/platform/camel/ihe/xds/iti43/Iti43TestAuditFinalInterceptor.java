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
package org.openehealth.ipf.platform.camel.ihe.xds.iti43;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.AuditTestFinalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.xds.iti43.audit.Iti43AuditDataset;

/**
 * Test XDS ATNA audit for ITI-43.
 * 
 * @author Dmytro Rud
 */
public class Iti43TestAuditFinalInterceptor extends AuditTestFinalInterceptor {

    public Iti43TestAuditFinalInterceptor(boolean isServerSide) {
        super(isServerSide, true);
    }


    @Override
    public void checkTransactionSpecificFields(AuditDataset auditDataset, boolean isServerSide) {
        Iti43AuditDataset iti43AuditDataset = (Iti43AuditDataset)auditDataset;
        
        String[] documentUuids      = iti43AuditDataset.getDocumentUuids();
        String[] repositoryUuids    = iti43AuditDataset.getRepositoryUuids();
        String[] homeCommunityUuids = iti43AuditDataset.getHomeCommunityUuids();

        assertNotNull(documentUuids);
        assertNotNull(repositoryUuids);
        assertNotNull(homeCommunityUuids);
        
        assertEquals(documentUuids.length, repositoryUuids.length);
        assertEquals(documentUuids.length, homeCommunityUuids.length);
    }
    
}