/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml;

import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;

/**
 * @author Dmytro Rud
 */
public interface EbXMLRegistryError {

    public String getCodeContext();
    public void setCodeContext(String codeContext);

    public String getErrorCode();
    public void setErrorCode(String errorCode);

    public Severity getSeverity();
    public void setSeverity(Severity severity);

    public String getLocation();
    public void setLocation(String location);
}
