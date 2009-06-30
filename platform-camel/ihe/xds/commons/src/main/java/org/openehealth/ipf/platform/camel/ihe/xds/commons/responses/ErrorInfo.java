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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.responses;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Contains information about an error.
 * @author Jens Riemschneider
 */
public class ErrorInfo {
    private ErrorCode errorCode;
    private String codeContext;
    private Severity serverity;
    private String location;
    
    public ErrorInfo() {}
    
    public ErrorInfo(ErrorCode errorCode, String codeContext, Severity serverity, String location) {
        this.errorCode = errorCode;
        this.codeContext = codeContext;
        this.serverity = serverity;
        this.location = location;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getCodeContext() {
        return codeContext;
    }
    
    public void setCodeContext(String codeContext) {
        this.codeContext = codeContext;
    }
    
    public Severity getServerity() {
        return serverity;
    }
    
    public void setServerity(Severity serverity) {
        this.serverity = serverity;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codeContext == null) ? 0 : codeContext.hashCode());
        result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((serverity == null) ? 0 : serverity.hashCode());
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
        ErrorInfo other = (ErrorInfo) obj;
        if (codeContext == null) {
            if (other.codeContext != null)
                return false;
        } else if (!codeContext.equals(other.codeContext))
            return false;
        if (errorCode == null) {
            if (other.errorCode != null)
                return false;
        } else if (!errorCode.equals(other.errorCode))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (serverity == null) {
            if (other.serverity != null)
                return false;
        } else if (!serverity.equals(other.serverity))
            return false;
        return true;
    }
    

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
