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
package org.openehealth.ipf.platform.manager.connection;

import java.io.Serializable;

/**
 * Serializable class representing a connection resource. In general, the
 * connection is not limeted to a JMX connection.
 * 
 * @see IJMXConnectionManager
 * 
 * @author Mitko Kolev
 */
public interface IConnectionConfiguration extends Serializable, Cloneable,
        Comparable<IConnectionConfiguration> {

    /**
     * Gets if this connection requires authentication credentials //
     * <em>[host[</em>:<em>port]][url-path]
     * 
     * @return
     */
    public AuthenticationCredentials getAuthenticationCredentials();

    /**
     * Returns the host of this connection //<em>[host[</em>:
     * <em>port]][url-path]
     * 
     * @return
     */
    public String getHost();

    /**
     * Returns the unique name of this connection //<em>[host[</em>:
     * <em>port]][url-path]
     * 
     * @return
     */
    public String getName();

    /**
     * Returns the port of this connection //<em>[host[</em>:
     * <em>port]][url-path]
     * 
     * @return
     */
    public int getPort();

    /**
     * Returns the hashcode of this connection according to the standard
     * hasCode-equals convention
     * 
     * @return
     */
    public int hashCode();

    /**
     * The equals method of this connection
     * 
     * @param o
     * @return
     */
    public boolean equals(Object o);

    /**
     * Create a deep copy of this object.
     * 
     * @return
     */
    public Object clone();
}
