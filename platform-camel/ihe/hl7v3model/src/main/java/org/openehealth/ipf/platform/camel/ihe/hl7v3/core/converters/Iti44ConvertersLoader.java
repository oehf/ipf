/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.core.converters;

import org.apache.camel.TypeConverterLoaderException;
import org.apache.camel.spi.TypeConverterLoader;
import org.apache.camel.spi.TypeConverterRegistry;
import org.apache.camel.support.SimpleTypeConverter;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientDuplicatesResolvedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordAddedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRevisedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.AcceptAcknowledgement;

/**
 * @since 6.1
 */
public final class Iti44ConvertersLoader implements TypeConverterLoader {

    public Iti44ConvertersLoader() {
    }

    @Override
    public void load(TypeConverterRegistry registry) throws TypeConverterLoaderException {
        registerConverters(registry);
    }

    private void registerConverters(TypeConverterRegistry registry) {
        addTypeConverter(registry, PatientRecordRequest.class, String.class, false,
            (type, exchange, value) -> Iti44Converters.xmlToPatientRecord((String) value));
        addTypeConverter(registry, String.class, PatientRecordAddedRequest.class, false,
            (type, exchange, value) -> Iti44Converters.recordAddedToXml((PatientRecordAddedRequest) value));
        addTypeConverter(registry, PatientRecordAddedRequest.class, String.class, false,
            (type, exchange, value) -> Iti44Converters.xmlToRecordAdded((String) value));
        addTypeConverter(registry, String.class, PatientRecordRevisedRequest.class, false,
            (type, exchange, value) -> Iti44Converters.recordRevisedToXml((PatientRecordRevisedRequest) value));
        addTypeConverter(registry, PatientRecordRevisedRequest.class, String.class, false,
            (type, exchange, value) -> Iti44Converters.xmlToRecordRevised((String) value));
        addTypeConverter(registry, String.class, PatientDuplicatesResolvedRequest.class, false,
            (type, exchange, value) -> Iti44Converters.duplicatesResolvedToXml((PatientDuplicatesResolvedRequest) value));
        addTypeConverter(registry, PatientDuplicatesResolvedRequest.class, String.class, false,
            (type, exchange, value) -> Iti44Converters.xmlToDuplicatesResolved((String) value));
        addTypeConverter(registry, String.class, AcceptAcknowledgement.class, false,
            (type, exchange, value) -> Iti44Converters.acknowledgementToXml((AcceptAcknowledgement) value));
        addTypeConverter(registry, AcceptAcknowledgement.class, String.class, false,
            (type, exchange, value) -> Iti44Converters.xmlToAcknowledgement((String) value));
    }

    private static void addTypeConverter(TypeConverterRegistry registry, Class<?> toType, Class<?> fromType, boolean allowNull, SimpleTypeConverter.ConversionMethod method) {
        registry.addTypeConverter(toType, fromType, new SimpleTypeConverter(allowNull, method));
    }

}
