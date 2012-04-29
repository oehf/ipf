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

import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.ErrorType;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
/**
 * Severities defined by the XDS specification.
 * @author Jens Riemschneider
 */
@XmlType(name = "Severity")
@XmlEnum(String.class)
public enum Severity {
    /** An error. */
    @XmlEnumValue("Error") ERROR(ErrorType.ERROR, "urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"),
    /** A warning. */
    @XmlEnumValue("Warning") WARNING(ErrorType.WARNING, "urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Warning");
    
    private final ErrorType ebXML21;
    private final String opcode30;
    
    private Severity(ErrorType opcode21, String opcode30) {
        ebXML21 = opcode21;
        this.opcode30 = opcode30;
    }

    /**
     * @return a representation in ebXML 2.1.
     */
    public ErrorType getEbXML21() {
        return ebXML21;
    }

    /**
     * @return a string representation in ebXML 3.1.
     */
    public String getOpcode30() {
        return opcode30;
    }
    
    /**
     * <code>null</code>-safe version of {@link #getEbXML21()}.
     * @param severity
     *          the type for which to get the ebXML representation. Can be <code>null</code>.
     * @return the ebXML 2.1 representation or <code>null</code> if type was <code>null</code>.
     */
    public static ErrorType getEbXML21(Severity severity) {
        return severity != null ? severity.getEbXML21() : null;
    }

    /**
     * <code>null</code>-safe version of {@link #getOpcode30()}.
     * @param severity
     *          the type for which to get the opcode. Can be <code>null</code>.
     * @return the opcode or <code>null</code> if type was <code>null</code>.
     */
    public static String getOpcode30(Severity severity) {
        return severity != null ? severity.getOpcode30() : null;
    }
    
    /**
     * Returns the severity that is represented by the given opcode.
     * <p>
     * This method looks up the opcode via the ebXML 3.0 representation.
     * @param opcode30
     *          the string representation. Can be <code>null</code>.
     * @return the severity.
     */
    public static Severity valueOfOpcode30(String opcode30) {
        for (Severity severity : values()) {
            if (severity.getOpcode30().equals(opcode30)) {
                return severity;
            }
        }
        
        throw new XDSMetaDataException(ValidationMessage.INVALID_SEVERITY_IN_RESPONSE);
    }

    /**
     * Returns the severity that is represented by the given ebXML 2.1.
     * @param opcode21
     *          the ebXML 2.1 representation. Can be <code>null</code>.
     * @return the severity.
     */
    public static Severity valueOfOpcode21(ErrorType opcode21) {
        for (Severity severity : values()) {
            if (severity.getEbXML21().equals(opcode21)) {
                return severity;
            }
        }
        
        throw new XDSMetaDataException(ValidationMessage.INVALID_SEVERITY_IN_RESPONSE);
    }
}
