/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.fhir.extend

import ca.uhn.fhir.context.FhirContext
import org.apache.camel.builder.DataFormatClause
import org.apache.camel.model.ProcessorDefinition
import org.openehealth.ipf.platform.camel.core.extend.CoreExtensionModule
import org.openehealth.ipf.platform.camel.ihe.fhir.datamodel.FhirDataFormat
import org.openehealth.ipf.platform.camel.ihe.fhir.datamodel.JsonFhirDataFormat
import org.openehealth.ipf.platform.camel.ihe.fhir.datamodel.XmlFhirDataFormat

/**
 *
 */
class FhirExtensionModule {

    static ProcessorDefinition fhirJson(DataFormatClause self) {
        fhirJson(self, null, null)
    }

    static ProcessorDefinition fhirJson(DataFormatClause self, FhirContext defaultContext, String defaultCharset) {
        registerDataFormat(new JsonFhirDataFormat(), self, defaultContext, defaultCharset)
    }

    static ProcessorDefinition fhirXml(DataFormatClause self) {
        fhirXml(self, null, null)
    }

    static ProcessorDefinition fhirXml(DataFormatClause self, FhirContext defaultContext, String defaultCharset) {
        registerDataFormat(new XmlFhirDataFormat(), self, defaultContext, defaultCharset)
    }

    private static void registerDataFormat(FhirDataFormat dataFormat, DataFormatClause self, FhirContext defaultContext, String defaultCharset) {
        if (defaultContext != null) dataFormat.setDefaultFhirContext(defaultContext)
        if (defaultCharset != null) dataFormat.setDefaultCharset(defaultCharset)
        CoreExtensionModule.dataFormatFor(self, dataFormat)
    }
}
