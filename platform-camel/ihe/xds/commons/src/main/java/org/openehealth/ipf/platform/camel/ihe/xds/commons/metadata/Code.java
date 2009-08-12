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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents a code.
 * <p> 
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
public class Code implements Serializable {
    private static final long serialVersionUID = 7603534956639945984L;
    
    private String code;
    private LocalizedString displayName;
    private String schemeName;
    
    /**
     * Constructs a code.
     */
    public Code() {}
    
    /**
     * Constructs a code.
     * @param code
     *          the value of the code.
     * @param displayName
     *          the display name of the code.
     * @param schemeName
     *          the schema of the code.
     */
    public Code(String code, LocalizedString displayName, String schemeName) {
        this.code = code;
        this.displayName = displayName;
        this.schemeName = schemeName;
    }

    /**
     * @return the value of this code.
     */
    public String getCode() {
        return code;
    }
    
    /**
     * @param code 
     *          the value of this code.
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    /**
     * @return the display name of this code.
     */
    public LocalizedString getDisplayName() {
        return displayName;
    }
    
    /**
     * @param displayName
     *          the display name of this code.
     */
    public void setDisplayName(LocalizedString displayName) {
        this.displayName = displayName;
    }
    
    /**
     * @return the schema of this code.
     */
    public String getSchemeName() {
        return schemeName;
    }
    
    /**
     * @param schemeName
     *          the schema of this code.
     */
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((schemeName == null) ? 0 : schemeName.hashCode());
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
        Code other = (Code) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (displayName == null) {
            if (other.displayName != null)
                return false;
        } else if (!displayName.equals(other.displayName))
            return false;
        if (schemeName == null) {
            if (other.schemeName != null)
                return false;
        } else if (!schemeName.equals(other.schemeName))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
