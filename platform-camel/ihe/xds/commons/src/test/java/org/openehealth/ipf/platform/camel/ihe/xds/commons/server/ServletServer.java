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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.server;

import static org.apache.commons.lang.Validate.notNull;

import java.io.File;

import javax.servlet.Servlet;

/**
 * Simple abstraction of an embedded servlet server (e.g. Jetty or Tomcat).
 * <p>
 * Note: any exceptions thrown are treated as assertion failures.
 * @author Jens Riemschneider
 */
public abstract class ServletServer {
    private Servlet servlet;
    private int port;
    private String contextPath;
    private String servletPath;
    private File contextFile;

    /**
     * Starts the server.
     * <p>
     * Note: any exceptions thrown are treated as assertion failures.
     */
    public abstract void start();
    
    /**
     * Stops the server if it was running.
     * <p>
     * Note: any exceptions thrown are treated as assertion failures.
     */
    public abstract void stop();

    /**
     * @param servletPath
     *          the path of the servlet itself (including patterns if needed).
     */
    public void setServletPath(String servletPath) {
        notNull(servletPath, "servletPath cannot be null");
        this.servletPath = servletPath;
    }

    /**
     * @return the path of the servlet itself (including patterns if needed).
     */
    public String getServletPath() {
        return servletPath;
    }

    /**
     * @param contextPath
     *          the path of the context in which the servlet is running.
     */
    public void setContextPath(String contextPath) {
        notNull(contextPath, "contextPath cannot be null");
        this.contextPath = contextPath;
    }

    /**
     * @return the path of the context in which the servlet is running.
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * @param port
     *          the port that the server is started on.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the port that the server is started on.
     */
    public int getPort() {
        return port;
    }
    
    /**
     * The servlet to use within the servlet.
     * @param servlet
     *          the servlet.      
     */
    public void setServlet(Servlet servlet) {
        notNull(servlet, "servlet cannot be null");
        this.servlet = servlet;
    }

    /**
     * @return the servlet to use within the servlet.
     */
    public Servlet getServlet() {
        return servlet;
    }

    /**
     * @return the location of the context file.
     */
    public File getContextFile() {
        return contextFile;
    }

    /**
     * @param contextFile
     *          the location of the context file.
     */
    public void setContextFile(File contextFile) {
        notNull(contextFile, "contextFile cannot be null");
        this.contextFile = contextFile;
    }
}
