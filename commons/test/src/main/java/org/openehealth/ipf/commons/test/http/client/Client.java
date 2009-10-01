/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.commons.test.http.client;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;

/**
 * @author Martin Krasser
 */
public class Client {

    private URL serverUrl;
    
    private String contentType;
    
    private ResponseHandler handler;
    
    private final HttpClient client;
    
    private final HttpConnectionManager mgr; 
    
    public Client() {
        this.mgr = new MultiThreadedHttpConnectionManager();
        this.client = new HttpClient(mgr);
        this.contentType = "text/plain";
    }
    
    public void setDefaultMaxConnectionsPerHost(int numConnections) {
        mgr.getParams().setDefaultMaxConnectionsPerHost(numConnections);
    }
    
    public int getDefaultMaxConnectionsPerHost() {
        return mgr.getParams().getDefaultMaxConnectionsPerHost();
    }
    
    public URL getServerUrl() {
        return serverUrl;
    }
    public void setServerUrl(URL serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public void setHandler(ResponseHandler handler) {
        this.handler = handler;
    }
    
    public void execute(InputStream input) throws Exception {
        PostMethod method = new PostMethod(serverUrl.toString());
        
        method.setRequestEntity(new InputStreamRequestEntity(input, contentType));
        client.executeMethod(method);
        
        try {
            InputStream responseStream = method.getResponseBodyAsStream();
            handler.handleResponse(responseStream);
            IOUtils.closeQuietly(responseStream);
        } finally {
            method.releaseConnection();
        }
        
    }
    
}
