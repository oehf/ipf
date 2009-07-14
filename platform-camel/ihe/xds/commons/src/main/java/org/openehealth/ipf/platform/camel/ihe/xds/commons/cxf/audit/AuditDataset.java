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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;

/**
 * A data structure used to store information pieces collected by various
 * auditing-related CXF interceptors.
 * 
 * @author Dmytro Rud
 */
public class AuditDataset {

    // whether we audit on server (true) or on client (false)
    private final boolean serverSide;

    // SOAP Body (XML) payload
    private String payload;
    // client user ID (WS-Addressing <Reply-To> header)
    private String userId;
    // client user name (WS-Security <Username> header)
    private String userName;
    // client IP address
    private String clientIpAddress;
    // service (i.e. registry or repository) endpoint URL
    private String serviceEndpointUrl;
    // patient ID as HL7 CX datatype, e.g. "1234^^^&1.2.3.4&ISO"
    private String patientId;
    // submission set unique ID
    private String submissionSetUuid;

    /**
     * Constructor.
     * 
     * @param isServerSide
     *            specifies whether this audit dataset will be used on the
     *            server side (<code>true</code>) or on the client side (
     *            <code>false</code>)
     */
    public AuditDataset(boolean isServerSide) {
        this.serverSide = isServerSide;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setServiceEndpointUrl(String serviceEntpointUrl) {
        this.serviceEndpointUrl = serviceEntpointUrl;
    }

    public String getServiceEndpointUrl() {
        return serviceEndpointUrl;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setSubmissionSetUniqueID(String submissionSetUuid) {
        this.submissionSetUuid = submissionSetUuid;
    }

    public String getSubmissionSetUuid() {
        return submissionSetUuid;
    }

    public boolean isServerSide() {
        return serverSide;
    }

    /**
     * <i>"What you see is what I get"</i>&nbsp;&mdash; returns a string that
     * consists from all fields available through getter methods.
     * 
     * @return string representation of this audit dataset
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        try {
            Method[] methods = this.getClass().getMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                Class<?> methodReturnType = method.getReturnType();

                if ((methodName.startsWith("get") || methodName.startsWith("is"))
                        && (method.getParameterTypes().length == 0)) {
                    sb.append("\n    ").append(method.getName()).append(" -> ");

                    if (methodReturnType == String[].class) {
                        String[] result = (String[]) method.invoke(this);
                        for (int i = 0; i < result.length; ++i) {
                            sb.append((i == 0) ? "{" : ", ").append(result[i]);
                        }
                        sb.append('}');

                    } else {
                        sb.append(method.invoke(this));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.append("\n]").toString();
    }

    /**
     * Checks whether this audit dataset contains non-null values in the fields
     * from the given list.
     * 
     * @param fieldNames
     *            a list of field names with first letter capitalized, e.g.
     *            "Address"
     * @param positiveCheck
     *            <code>true</code> when the given fields must be present;
     *            <code>false</code> when they must be absent.
     * @return a set of names of the fields which do not match the given
     *         condition (i.e. are absent when they must be present, and vice
     *         versa).
     * @throws Exception
     *             on reflection errors
     */
    public Set<String> checkFields(String[] fieldNames, boolean positiveCheck) throws Exception {
        Set<String> result = new HashSet<String>();

        for (String fieldName : fieldNames) {
            Method m = getClass().getMethod("get" + fieldName);
            Object o = m.invoke(this);
            if ((o == null) == positiveCheck) {
                result.add(fieldName);
            }
        }

        return result;
    }

    /**
     * Enriches the set with fields extracted from a submit objects request POJO.
     * 
     * @param request
     *      a {@link EbXMLSubmitObjectsRequest} as POJO 
     */
    public void enrichDatasetFromSubmitObjectsRequest(EbXMLSubmitObjectsRequest ebXML) 
    {
        List<EbXMLRegistryPackage> submissionSets = 
            ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE);
        
        for (EbXMLRegistryPackage submissionSet : submissionSets) {
            String patientID = submissionSet.getExternalIdentifierValue(
                    Vocabulary.SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID);            
            setPatientId(patientID);
            
            String uniqueID = submissionSet.getExternalIdentifierValue(
                    Vocabulary.SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID);
            setSubmissionSetUniqueID(uniqueID);
        }
    }
}
