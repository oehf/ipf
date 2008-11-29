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

/**
 * The class is stateless and can be serialized.
 * 
 * @see IConnectionConfiguration
 * 
 * @author Mitko Kolev
 */
public class ConnectionConfigurationImpl implements IConnectionConfiguration {

    private static final long serialVersionUID = -5662511690282050211L;

    private final AuthenticationCredentials credentials;

    private final String host;

    private final int port;

    private final String name;

    /**
     * Creates a connection instance with the given name. In order this
     * connection to be put in the repository of connection the given connection
     * name must be repository unique.
     * 
     * @param name
     *            the repository unique name of the connection
     * @param host
     *            the host of the target.
     * @param port
     *            the port of the target.
     */
    public ConnectionConfigurationImpl(String name, String host, int port) {
        this(name, host, port, new AuthenticationCredentials("", ""));
    }

    public ConnectionConfigurationImpl(String name, String host, int port,
            String userName, String password) {
        this(name, host, port,
                new AuthenticationCredentials(userName, password));
    }

    /**
     * @see ConnectionConfigurationImpl#ConnectionImpl(String, String, int)
     */
    public ConnectionConfigurationImpl(String name, String host, int port,
            AuthenticationCredentials credentials) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.credentials = (AuthenticationCredentials) credentials.clone();
    }

    @Override
    /*
     * @see AuthenticationCredentials
     */
    public AuthenticationCredentials getAuthenticationCredentials() {
        return credentials;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public Object clone() {
        ConnectionConfigurationImpl connection = new ConnectionConfigurationImpl(
                this.name, this.host, this.port, this.credentials);
        return connection;
    }

    @Override
    public String toString() {
        StringBuffer connection = new StringBuffer("");
        if (this.getAuthenticationCredentials().isValid()) {
            String userName = this.getAuthenticationCredentials().getUserName();
            if (userName != null) {
                connection.append(
                        this.getAuthenticationCredentials().getUserName())
                        .append("@");
            }
        }
        if (host != null) {
            connection.append(this.getHost()).append(":")
                    .append(this.getPort());
        }
        return connection.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ConnectionConfigurationImpl other = (ConnectionConfigurationImpl) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(IConnectionConfiguration o) {
        if (o == null)
            return -1;
        if (o.getName() == null) {
            return -1;
        }
        return this.name.compareTo(o.getName());
    }
}
