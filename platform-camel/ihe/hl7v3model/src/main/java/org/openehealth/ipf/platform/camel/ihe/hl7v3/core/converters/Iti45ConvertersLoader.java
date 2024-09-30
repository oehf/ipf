/*
 * Copyright 2021 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PixV3QueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PixV3QueryResponse;

/**
 * @since 4.1
 */
public final class Iti45ConvertersLoader implements TypeConverterLoader {

    public Iti45ConvertersLoader() {
    }

    @Override
    public void load(TypeConverterRegistry registry) throws TypeConverterLoaderException {
        registerConverters(registry);
    }

    private void registerConverters(TypeConverterRegistry registry) {
        addTypeConverter(registry, String.class, PixV3QueryRequest.class, false,
            (type, exchange, value) -> Iti45Converters.simpleRequestToXml((PixV3QueryRequest) value));
        addTypeConverter(registry, String.class, PixV3QueryResponse.class, false,
            (type, exchange, value) -> Iti45Converters.simpleResponseToXml((PixV3QueryResponse) value));
        addTypeConverter(registry, PixV3QueryRequest.class, String.class, false,
            (type, exchange, value) -> Iti45Converters.xmlToSimpleRequest((String) value));
        addTypeConverter(registry, PixV3QueryResponse.class, String.class, false,
            (type, exchange, value) -> Iti45Converters.xmlToSimpleResponse((String) value));
    }

    private static void addTypeConverter(TypeConverterRegistry registry, Class<?> toType, Class<?> fromType, boolean allowNull, SimpleTypeConverter.ConversionMethod method) { 
        registry.addTypeConverter(toType, fromType, new SimpleTypeConverter(allowNull, method));
    }

}
