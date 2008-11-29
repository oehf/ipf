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
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of @see {@link AuthenticationCredentials}
 * 
 * @see IConnectionConfiguration
 * 
 * @author Mitko Kolev
 */
public class AuthenticationCredentials implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3571396760752752173L;

    private String userName;

    private String password;

    public AuthenticationCredentials(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Changes the password.
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the user name
     * 
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result
                + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AuthenticationCredentials other = (AuthenticationCredentials) obj;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        return true;
    }

    public String[] toStringArray() {
        List<String> credentials = new ArrayList<String>();
        credentials.add(userName);
        credentials.add(password);
        // create always a new array
        return credentials.toArray(new String[0]);
    }

    public boolean isValid() {
        if (this.userName == null || this.userName.trim().equals(""))
            return false;
        if (this.password == null || this.password.trim().equals(""))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        return new AuthenticationCredentials(userName, password);
    }
}
