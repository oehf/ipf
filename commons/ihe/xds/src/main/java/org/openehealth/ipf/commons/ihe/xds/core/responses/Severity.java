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
package org.openehealth.ipf.commons.ihe.xds.core.responses;

import groovy.transform.EqualsAndHashCode;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XdsEnum;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.ErrorType;

import javax.xml.bind.annotation.XmlType;

/**
 * Severities defined by the XDS specification.
 * @author Jens Riemschneider
 */
@XmlType(name = "Severity")
@EqualsAndHashCode(callSuper = true)
public class Severity extends XdsEnum {
    private static final long serialVersionUID = 8543688612834139650L;

    /** An error. */
    public static final Severity ERROR = new Severity(Type.OFFICIAL, ErrorType.ERROR, "urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error");
    /** A warning. */
    public static final Severity WARNING = new Severity(Type.OFFICIAL, ErrorType.WARNING, "urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Warning");

    public static final Severity[] OFFICIAL_VALUES = {ERROR, WARNING};

    @Getter private final ErrorType ebXML21ErrorType;

    public Severity(Type type, ErrorType ebXML21ErrorType, String ebXML30) {
        super(type, ebXML30);
        this.ebXML21ErrorType = ebXML21ErrorType;
    }

    @Override
    public String getJaxbValue() {
        return (getType() == Type.OFFICIAL) ? getEbXML21ErrorType().value() : getEbXML30();
    }
}
