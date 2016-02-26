/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.tutorials.ref;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.InputStream;
import java.net.URL;

/**
 * @author Martin Krasser
 */
public class Client {

    private URL serverUrl;
    
    private ContentType contentType;
    
    private ResponseHandler handler;
    
    private final CloseableHttpClient client;

    private final PoolingHttpClientConnectionManager mgr;
    
    public Client() {
        mgr = new PoolingHttpClientConnectionManager();
        client = HttpClients.custom()
                .setConnectionManager(mgr)
                .build();
        contentType = ContentType.TEXT_PLAIN;
    }
    
    public void setDefaultMaxConnectionsPerHost(int numConnections) {
        mgr.setDefaultMaxPerRoute(numConnections);
    }
    
    public int getDefaultMaxConnectionsPerHost() {
        return mgr.getDefaultMaxPerRoute();
    }
    
    public URL getServerUrl() {
        return serverUrl;
    }
    public void setServerUrl(URL serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setContentType(String contentType) {
        this.contentType = ContentType.create(contentType);
    }
    
    public void setHandler(ResponseHandler handler) {
        this.handler = handler;
    }
    
    public void execute(InputStream input) throws Exception {
        HttpPost method = new HttpPost(serverUrl.toString());
        method.setEntity(new InputStreamEntity(input, contentType));
        CloseableHttpResponse response = client.execute(method);
        
        try {
            InputStream responseStream = response.getEntity().getContent();
            handler.handleResponse(responseStream);
            IOUtils.closeQuietly(responseStream);
        } finally {
            method.releaseConnection();
        }
        
    }
    
}
