/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.mllp.custom;

import ca.uhn.hl7v2.ErrorCode;
import ca.uhn.hl7v2.Version;
import ca.uhn.hl7v2.conf.store.ProfileStoreFactory;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.validation.builder.support.DefaultValidationWithoutTNBuilder;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.MllpAuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * Factory bean for a {@link Hl7v2TransactionConfiguration} instance.
 */
public class MdmHl7v2TransactionConfigurationFactoryBean implements FactoryBean<Hl7v2TransactionConfiguration> {

    @Override
    public Hl7v2TransactionConfiguration getObject() {
        return new Hl7v2TransactionConfiguration<>(
                "mdm",
                "Medical Document Management",
                false,
                null,
                null,
                new Version[] {Version.V25, Version.V251},
                "MDM Adapter",
                "IPF",
                ErrorCode.APPLICATION_INTERNAL_ERROR,
                ErrorCode.APPLICATION_INTERNAL_ERROR,
                new String[] { "MDM" },
                new String[] { "T01 T02 T03 T04"},
                new String[] {"ACK"},
                new String[] {"*"},
                new boolean[] {true},
                new boolean[] {false},
                HapiContextFactory.createHapiContext(
                        new DefaultModelClassFactory(),
                        new DefaultValidationWithoutTNBuilder(),
                        ProfileStoreFactory.getProfileStore()));
    }

    @Override
    public Class<?> getObjectType() {
        return Hl7v2TransactionConfiguration.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
