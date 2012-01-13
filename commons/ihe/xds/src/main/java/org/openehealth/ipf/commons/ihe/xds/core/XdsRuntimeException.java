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
package org.openehealth.ipf.commons.ihe.xds.core;

import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;

/**
 * @author Dmytro Rud
 */
public class XdsRuntimeException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String codeContext;
    private final Severity severity;
    private final String location;

    public XdsRuntimeException(
            ErrorCode errorCode,
            String codeContext,
            Severity severity,
            String location)
    {
        super();
        this.errorCode   = Validate.notNull(errorCode);
        this.codeContext = Validate.notEmpty(codeContext);
        this.severity    = Validate.notNull(severity);
        this.location    = location;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getCodeContext() {
        return codeContext;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder()
                .append((severity == Severity.ERROR) ? "Error " : "Warning ")
                .append(errorCode.getOpcode())
                .append(": ")
                .append(codeContext);
        if (location != null) {
            sb.append(". Location: ").append(location);
        }
        return sb.toString();
    }
}
