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
package org.openehealth.ipf.commons.test.http.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;

/**
 * @author Martin Krasser
 */
public class GenericHandler extends AbstractHandler {

    private RequestHandler delegateHandler;
    
    public void setDelegateHandler(RequestHandler delegateHandler) {
        this.delegateHandler = delegateHandler;
    }

    @Override
    public void handle(String target,
            HttpServletRequest request,
            HttpServletResponse response, 
            int dispatch) throws IOException, ServletException {
    	
    	Request base_request = (request instanceof Request) ? (Request)request:HttpConnection.getCurrentConnection().getRequest();
		base_request.setHandled(true);
		delegateHandler.handle(request, response);
        response.getWriter().flush();
    }

}
