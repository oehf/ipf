/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3;

import org.springframework.beans.PropertyAccessorFactory;

import java.util.Map;

/**
 * Hl7v3-specific exception class.
 * Constructors are optimized for the Groovy's named parameters syntax.
 * 
 * @author Dmytro Rud
 */
public class Hl7v3Exception extends RuntimeException {
    private String typeCode = "AE";
    private String acknowledgementDetailCode = "INTERR";
    private String queryResponseCode = "AE";
    private String statusCode = "aborted";

    private String detectedIssueEventCode = "ActAdministrativeDetectedIssueCode";
    private String detectedIssueEventCodeSystem = "2.16.840.1.113883.5.4";
    private String detectedIssueManagementCode;
    private String detectedIssueManagementCodeSystem = "2.16.840.1.113883.5.4";


    public Hl7v3Exception(String message, Map<String, String> params) {
        super(message);
        initializeParams(params);
    }

    public Hl7v3Exception(String message, Throwable cause, Map<String, String> params) {
        super(message, cause);
        initializeParams(params);
    }

    public Hl7v3Exception(Throwable cause, Map<String, String> params) {
        super(cause);
        initializeParams(params);
    }

    private void initializeParams(Map<String, String> params) {
        PropertyAccessorFactory.forBeanPropertyAccess(this).setPropertyValues(params);
    }


    // ----- getters and setters -----

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getAcknowledgementDetailCode() {
        return acknowledgementDetailCode;
    }

    public void setAcknowledgementDetailCode(String acknowledgementDetailCode) {
        this.acknowledgementDetailCode = acknowledgementDetailCode;
    }

    public String getQueryResponseCode() {
        return queryResponseCode;
    }

    public void setQueryResponseCode(String queryResponseCode) {
        this.queryResponseCode = queryResponseCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDetectedIssueEventCode() {
        return detectedIssueEventCode;
    }

    public void setDetectedIssueEventCode(String detectedIssueEventCode) {
        this.detectedIssueEventCode = detectedIssueEventCode;
    }

    public String getDetectedIssueEventCodeSystem() {
        return detectedIssueEventCodeSystem;
    }

    public void setDetectedIssueEventCodeSystem(String detectedIssueEventCodeSystem) {
        this.detectedIssueEventCodeSystem = detectedIssueEventCodeSystem;
    }

    public String getDetectedIssueManagementCode() {
        return detectedIssueManagementCode;
    }

    public void setDetectedIssueManagementCode(String detectedIssueManagementCode) {
        this.detectedIssueManagementCode = detectedIssueManagementCode;
    }

    public String getDetectedIssueManagementCodeSystem() {
        return detectedIssueManagementCodeSystem;
    }

    public void setDetectedIssueManagementCodeSystem(String detectedIssueManagementCodeSystem) {
        this.detectedIssueManagementCodeSystem = detectedIssueManagementCodeSystem;
    }
}
