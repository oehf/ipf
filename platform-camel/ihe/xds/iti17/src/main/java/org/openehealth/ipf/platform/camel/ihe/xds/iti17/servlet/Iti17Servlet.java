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
package org.openehealth.ipf.platform.camel.ihe.xds.iti17.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.openehealth.ipf.platform.camel.ihe.xds.iti17.component.Iti17Consumer;

/**
 * Servlet implementation to dispatch incoming ITI-17 (Retrieve Document)
 * requests.
 * @author Jens Riemschneider
 */
public class Iti17Servlet extends HttpServlet {
    private static final long serialVersionUID = -401129606220792110L;
    
    private final static Map<String, Iti17Consumer> consumers = new ConcurrentHashMap<String, Iti17Consumer>();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        Iti17Consumer consumer = getConsumer(requestURI);
        if (consumer == null) {
            super.doGet(req, resp);
            return;
        }
        
        String fullRequestUri = req.getRequestURL()
            .append("?")
            .append(req.getQueryString())
            .toString();
        
        InputStream inputStream;
        try {
            inputStream = consumer.process(fullRequestUri);
        }        
        catch (Exception e) {
            resp.setStatus(500);
            return;
        }

        try {
            resp.setStatus(200);
            IOUtils.copy(inputStream, resp.getOutputStream());
        }
        finally {
            inputStream.close();
        }
    }

    private Iti17Consumer getConsumer(String requestURI) {
        for (Map.Entry<String, Iti17Consumer> entry : consumers.entrySet()) {
            if (requestURI.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Adds a consumer to a specific service address.
     * @param serviceAddress
     *          the address of the service to add the consumer to.
     * @param iti17Consumer
     *          the consumer to add.
     */
    public static void addConsumer(String serviceAddress, Iti17Consumer iti17Consumer) {
        consumers.put(serviceAddress, iti17Consumer);
    }
}
