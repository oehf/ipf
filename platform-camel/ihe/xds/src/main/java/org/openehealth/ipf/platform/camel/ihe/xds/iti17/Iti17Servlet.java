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
package org.openehealth.ipf.platform.camel.ihe.xds.iti17;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.CamelContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet implementation to dispatch incoming ITI-17 (Retrieve Document)
 * requests.
 * @author Jens Riemschneider
 */
public class Iti17Servlet extends HttpServlet {
    private static final long serialVersionUID = -401129606220792110L;
    
    private CamelContext context;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	CamelContext camelContext = getContext() != null ? getContext() : getCamelContext();

        String requestURI = req.getRequestURI();        
        String endpointName = toEndpointName(requestURI);
        
        Iti17Endpoint endpoint = camelContext.getEndpoint(endpointName, Iti17Endpoint.class);
        if (endpoint == null) {
            super.doGet(req, resp);
            return;
        }

        Iti17Consumer consumer = endpoint.getActiveConsumer();
        if (consumer == null) {
            super.doGet(req, resp);
            return;
        }
        
        String fullRequestURI = req.getRequestURL()
            .append("?")
            .append(req.getQueryString())
            .toString();
    
        InputStream inputStream;
        try {
            inputStream = consumer.process(fullRequestURI);
        }        
        catch (Throwable e) {
            resp.setStatus(500);
            audit(false, req.getRemoteAddr(), fullRequestURI);
            return;
        }
        
        if (inputStream != null) {
            try {
                resp.setStatus(200);
                IOUtils.copy(inputStream, resp.getOutputStream());
            }
            finally {
                IOUtils.closeQuietly(inputStream);
                audit(true, req.getRemoteAddr(), fullRequestURI);
            }
        }
        else {
            resp.setStatus(404);
            audit(false, req.getRemoteAddr(), fullRequestURI);
        }
    }

    private String toEndpointName(String requestURI) {
        String serviceName = requestURI.startsWith("/") ? requestURI.substring(1) : requestURI;
        return "xds-iti17:" + serviceName;
    }

    private CamelContext getCamelContext() {
        ServletContext servletContext = getServletContext();
        ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        Map<?, ?> camelContextBeans = appContext.getBeansOfType(CamelContext.class);
        Validate.isTrue(camelContextBeans.size() == 1, "A single camelContext bean is required in the application context");
        CamelContext camelContext = (CamelContext) camelContextBeans.values().iterator().next();
        Validate.notNull(camelContext, "A single camelContext bean is required in the application context");
        return camelContext;
    }
    
    private void audit(boolean successful, String clientIpAddress, String documentUri) {
        AuditorManager.getRepositoryAuditor().auditRetrieveDocumentEvent(
                successful ? RFC3881EventOutcomeCodes.SUCCESS : RFC3881EventOutcomeCodes.MAJOR_FAILURE,
                clientIpAddress,
                /*userName*/ null,
                documentUri, 
                /*documentUniqueId*/ null);
    }

	public CamelContext getContext() {
		return context;
	}

	public void setContext(CamelContext context) {
		this.context = context;
	}
}
