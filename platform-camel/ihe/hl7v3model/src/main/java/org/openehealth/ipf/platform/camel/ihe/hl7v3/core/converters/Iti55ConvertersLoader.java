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
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.XcpdQueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.XcpdQueryResponse;

/**
 * @since 6.1
 */
public final class Iti55ConvertersLoader implements TypeConverterLoader {

    public Iti55ConvertersLoader() {
    }

    @Override
    public void load(TypeConverterRegistry registry) throws TypeConverterLoaderException {
        registerConverters(registry);
    }

    private void registerConverters(TypeConverterRegistry registry) {
        addTypeConverter(registry, String.class, XcpdQueryRequest.class, false,
            (type, exchange, value) -> Iti55Converters.simpleRequestToXml((XcpdQueryRequest) value));
        addTypeConverter(registry, String.class, XcpdQueryResponse.class, false,
            (type, exchange, value) -> Iti55Converters.simpleResponseToXml((XcpdQueryResponse) value));
        addTypeConverter(registry, XcpdQueryRequest.class, String.class, false,
            (type, exchange, value) -> Iti55Converters.xmlToSimpleRequest((String) value));
        addTypeConverter(registry, XcpdQueryResponse.class, String.class, false,
            (type, exchange, value) -> Iti55Converters.xmlToSimpleResponse((String) value));
    }

    private static void addTypeConverter(TypeConverterRegistry registry, Class<?> toType, Class<?> fromType, boolean allowNull, SimpleTypeConverter.ConversionMethod method) {
        registry.addTypeConverter(toType, fromType, new SimpleTypeConverter(allowNull, method));
    }

}
