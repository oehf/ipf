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
package org.openehealth.ipf.platform.camel.ihe.xdsb.commons.cxf.audit;

import java.lang.reflect.Method;

/**
 * A data structure used to store information pieces collected 
 * by various auditing-related CXF interceptors.  
 * 
 * @author Dmytro Rud
 */
public class AuditDataset {

    // whether we audit on server (true) or on client (false) 
    private boolean isServerSide; 
    
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


    /**
     * Constructor.
     * 
     * @param isServerSide
     *      specifies whether this audit dataset will be used on the server 
     *      side (<code>true</code>) or on the client side (<code>false</code>) 
     */
    public AuditDataset(boolean isServerSide) {
        this.isServerSide = isServerSide;
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
    
    
    /**
     * <i>"What you see is what I get"</i>&nbsp;&mdash; returns a string that consists 
     * from all fields available through getter methods, exclusive <code>getClass()</code>.
     * 
     * @return
     *      string representation of this audit dataset
     */
    public String toString() {
        StringBuilder sb = new StringBuilder()
            .append(getClass().getCanonicalName())
            .append(" [");
        
        try {
            Method[] methods = this.getClass().getMethods();
            for(Method method : methods) {
                String methodName = method.getName();
                Class methodReturnType = method.getReturnType();
                
                if(methodName.startsWith("get") && 
                  ( ! "getClass".equals(methodName)) &&
                  (method.getParameterTypes().length == 0))
                {
                    sb
                        .append("\n    ")
                        .append(method.getName().substring(3))
                        .append(" -> ");
                    
                    if(methodReturnType == String[].class) {
                        String[] result = (String[])method.invoke(this);
                        for(int i = 0; i < result.length; ++i) {
                            sb
                                .append((i == 0) ? "{" : ", ")
                                .append(result[i]);
                        }
                        sb.append('}');
                        
                    } else {
                        sb.append(method.invoke(this));
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sb.append("\n]").toString();
    }


    /**
     * Returns the role (from the CXF point of view) of the participant
     * whose activities are being audited using this audit dataset.
     * 
     * @return
     *      either "server" or "client"
     */
    public String getRole() {
        return this.isServerSide ? "server" : "client";
    }
}
