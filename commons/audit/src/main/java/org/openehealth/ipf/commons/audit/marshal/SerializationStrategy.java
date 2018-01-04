/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.audit.marshal;


import org.openehealth.ipf.commons.audit.model.AuditMessage;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author Christian Ohr
 */
public interface SerializationStrategy {

    void marshal(AuditMessage auditMessage, Writer writer, boolean pretty) throws IOException;

    default String marshal(AuditMessage auditMessage, boolean pretty) {
        try {
            StringWriter writer = new StringWriter();
            marshal(auditMessage, writer, pretty);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
