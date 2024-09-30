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

import net.ihe.gazelle.hl7v3.mcciin000002UV01.MCCIIN000002UV01Type;
import net.ihe.gazelle.hl7v3.prpain201301UV02.PRPAIN201301UV02Type;
import net.ihe.gazelle.hl7v3.prpain201302UV02.PRPAIN201302UV02Type;
import net.ihe.gazelle.hl7v3.prpain201304UV02.PRPAIN201304UV02Type;
import net.ihe.gazelle.hl7v3.prpain201305UV02.PRPAIN201305UV02Type;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02Type;
import net.ihe.gazelle.hl7v3.prpain201309UV02.PRPAIN201309UV02Type;
import net.ihe.gazelle.hl7v3.prpain201310UV02.PRPAIN201310UV02Type;
import net.ihe.gazelle.hl7v3.quqiin000003UV01.QUQIIN000003UV01CancelType;
import net.ihe.gazelle.hl7v3.quqiin000003UV01.QUQIIN000003UV01Type;
import org.apache.camel.TypeConverterLoaderException;
import org.apache.camel.spi.TypeConverterLoader;
import org.apache.camel.spi.TypeConverterRegistry;
import org.apache.camel.support.SimpleTypeConverter;

/**
 * @since 4.1
 */
public final class JaxbHl7v3ConvertersLoader implements TypeConverterLoader {

    public JaxbHl7v3ConvertersLoader() {
    }

    @Override
    public void load(TypeConverterRegistry registry) throws TypeConverterLoaderException {
        registerConverters(registry);
    }

    private void registerConverters(TypeConverterRegistry registry) {
        addTypeConverter(registry, String.class, PRPAIN201301UV02Type.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.PRPAIN201301UV02toXml((PRPAIN201301UV02Type) value));
        addTypeConverter(registry, PRPAIN201301UV02Type.class, String.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.xmlToPRPAIN201301UV02((String) value));

        addTypeConverter(registry, String.class, PRPAIN201302UV02Type.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.PRPAIN201302UV02toXml((PRPAIN201302UV02Type) value));
        addTypeConverter(registry, PRPAIN201302UV02Type.class, String.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.xmlToPRPAIN201302UV02((String) value));

        addTypeConverter(registry, String.class, PRPAIN201304UV02Type.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.PRPAIN201304UV02toXml((PRPAIN201304UV02Type) value));
        addTypeConverter(registry, PRPAIN201304UV02Type.class, String.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.xmlToPRPAIN201304UV02((String) value));

        addTypeConverter(registry, String.class, PRPAIN201305UV02Type.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.PRPAIN201305UV02toXml((PRPAIN201305UV02Type) value));
        addTypeConverter(registry, PRPAIN201305UV02Type.class, String.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.xmlToPRPAIN201305UV02((String) value));

        addTypeConverter(registry, String.class, PRPAIN201306UV02Type.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.PRPAIN201306UV02toXml((PRPAIN201306UV02Type) value));
        addTypeConverter(registry, PRPAIN201306UV02Type.class, String.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.xmlToPRPAIN201306UV02((String) value));

        addTypeConverter(registry, String.class, PRPAIN201309UV02Type.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.PRPAIN201309UV02toXml((PRPAIN201309UV02Type) value));
        addTypeConverter(registry, PRPAIN201309UV02Type.class, String.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.xmlToPRPAIN201309UV02((String) value));

        addTypeConverter(registry, String.class, PRPAIN201310UV02Type.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.PRPAIN201310UV02toXml((PRPAIN201310UV02Type) value));
        addTypeConverter(registry, PRPAIN201310UV02Type.class, String.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.xmlToPRPAIN201310UV02((String) value));

        addTypeConverter(registry, String.class, MCCIIN000002UV01Type.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.MCCIIN000002UV01toXml((MCCIIN000002UV01Type) value));
        addTypeConverter(registry, MCCIIN000002UV01Type.class, String.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.xmlToMCCIIN000002UV01((String) value));

        addTypeConverter(registry, String.class, QUQIIN000003UV01Type.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.QUQIIN000003UV01toXml((QUQIIN000003UV01Type) value));
        addTypeConverter(registry, QUQIIN000003UV01Type.class, String.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.xmlToQUQIIN000003UV01((String) value));

        addTypeConverter(registry, String.class, QUQIIN000003UV01CancelType.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.QUQIIN000003UV01CancelToXml((QUQIIN000003UV01CancelType) value));
        addTypeConverter(registry, QUQIIN000003UV01CancelType.class, String.class, false,
            (type, exchange, value) -> JaxbHl7v3Converters.xmlToQUQIIN000003UV01Cancel((String) value));
    }

    private static void addTypeConverter(TypeConverterRegistry registry, Class<?> toType, Class<?> fromType, boolean allowNull, SimpleTypeConverter.ConversionMethod method) {
        registry.addTypeConverter(toType, fromType, new SimpleTypeConverter(allowNull, method));
    }

}
