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

package org.openehealth.ipf.commons.ihe.xds.core.server;

import static org.apache.commons.lang.Validate.notNull;

import org.apache.catalina.core.StandardWrapper;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * Wrapper that is used as a replacement to {@link StandardWrapper} to inject
 * a predefined servlet instead of using a new instance.
 * <p>
 * This class allows the {@link TomcatServer} to inject an already configured
 * servlet instead of using a servlet type and dynamic creation of the servlet.
 * @author Jens Riemschneider
 */
public class TomcatServletWrapper extends StandardWrapper {
    private static final long serialVersionUID = 5267781925243849228L;
    
    private static Servlet servlet;

    /**
     * Sets the servlet to be used by the wrapper.
     * @param servlet
     *          the servlet.
     */
    public static void setServlet(Servlet servlet) {
        notNull(servlet, "servlet cannot be null");
        TomcatServletWrapper.servlet = servlet;
    }

    @Override
    public Servlet loadServlet() throws ServletException {
        servlet.init(facade);
        return servlet;
    }
}
