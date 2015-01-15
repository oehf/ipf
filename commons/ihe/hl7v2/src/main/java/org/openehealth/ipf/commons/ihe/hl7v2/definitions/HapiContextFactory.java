/*
 * Copyright 2014 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.hl7v2.definitions;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.hl7v2.util.idgenerator.FileBasedHiLoGenerator;
import ca.uhn.hl7v2.util.idgenerator.IDGenerator;
import ca.uhn.hl7v2.validation.impl.SimpleValidationExceptionHandler;
import org.openehealth.ipf.gazelle.validation.profile.HL7v2Transactions;
import org.openehealth.ipf.gazelle.validation.profile.store.GazelleProfileStore;

/**
 * This factory creates HapiContext instances that are required to derive {@link ca.uhn.hl7v2.parser.ModelClassFactory}
 * and {@link ca.uhn.hl7v2.validation.ValidationContext} valid for the given
 * {@link org.openehealth.ipf.gazelle.validation.profile.HL7v2Transactions HL7v2 transaction}.
 * <p>
 * By default, the HapiContext defines the transaction-specific conformance profile and profile store, and
 * disables validation during parsing.
 * </p>
 */
public class HapiContextFactory {

    private static IDGenerator idGenerator = new FileBasedHiLoGenerator();

    /**
     * Allows to globally set the {@link ca.uhn.hl7v2.util.idgenerator.IDGenerator} that generates
     * IDs for new HL7 messages from new HapiContext instances. This does not affect the ID generation
     * of already created HapiContexts nor does it do any cleanup of the previous generator.
     *
     * @param generator global ID generator
     */
    public static synchronized void setIdGenerator(IDGenerator generator) {
        idGenerator = generator;
    }

    /**
     * Returns a HapiContext for the provided
     * {@link org.openehealth.ipf.gazelle.validation.profile.HL7v2Transactions HL7v2 transaction}, using
     * a {@link ca.uhn.hl7v2.parser.DefaultModelClassFactory default HL7 model}
     *
     * @param transactions profile enumeration
     * @return HapiContext
     */
    public static HapiContext createHapiContext(HL7v2Transactions transactions) {
        return createHapiContext(new DefaultModelClassFactory(), transactions);
    }

    /**
     * Returns a HapiContext for the provided
     * {@link org.openehealth.ipf.gazelle.validation.profile.HL7v2Transactions HL7v2 transaction}, using
     * a custom HL7 model.
     *
     * @param modelClassFactory transaction-specific model-class factory
     * @param transactions      profile enumeration
     * @return HapiContext
     */
    public static HapiContext createHapiContext(ModelClassFactory modelClassFactory, HL7v2Transactions transactions) {
        HapiContext context = new DefaultHapiContext(modelClassFactory);
        context.setProfileStore(new GazelleProfileStore());
        context.setValidationRuleBuilder(new ConformanceProfileBasedValidationBuilder(transactions));
        context.getParserConfiguration().setValidating(false);
        context.getParserConfiguration().setIdGenerator(idGenerator);
        context.setValidationExceptionHandlerFactory(new SimpleValidationExceptionHandler(context));
        return context;
    }
}
