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
package org.openehealth.ipf.commons.test.http.server.handler;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openehealth.ipf.commons.test.http.server.RequestHandler;

/**
 * @author Martin Krasser
 */
public class DefaultHandler implements RequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultHandler.class);
    

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream requestStream = request.getInputStream();
        String result = IOUtils.toString(requestStream);
        response.getWriter().println(result);
        response.setContentType("text/plain");
        if (LOG.isInfoEnabled()){
            LOG.info(result);
        }
        IOUtils.closeQuietly(requestStream);
    }

}
