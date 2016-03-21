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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryError;

/**
 * @author Dmytro Rud
 */
public class EbXMLRegistryError30 implements EbXMLRegistryError {
    private final RegistryError error;

    public EbXMLRegistryError30(RegistryError error) {
        Validate.notNull(error, "registry error object cannot be null");
        this.error = error;
    }

    @Override
    public String getCodeContext() {
        return error.getCodeContext();
    }

    @Override
    public void setCodeContext(String codeContext) {
        error.setCodeContext(codeContext);
    }

    @Override
    public String getErrorCode() {
        return error.getErrorCode();
    }

    @Override
    public void setErrorCode(String errorCode) {
        error.setErrorCode(errorCode);
    }

    @Override
    public Severity getSeverity() {
        return Severity.valueOfOpcode30(error.getSeverity());
    }

    @Override
    public void setSeverity(Severity severity) {
        error.setSeverity(severity.getOpcode30());
    }

    @Override
    public String getLocation() {
        return error.getLocation();
    }

    @Override
    public void setLocation(String location) {
        error.setLocation(location);
    }

    public RegistryError getInternal() {
        return error;
    }
}
