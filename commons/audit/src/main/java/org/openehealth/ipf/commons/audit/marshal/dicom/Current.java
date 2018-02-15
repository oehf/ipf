/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.audit.marshal.dicom;


import org.openehealth.ipf.commons.audit.marshal.SerializationStrategy;
import org.openehealth.ipf.commons.audit.model.AuditMessage;

import java.io.IOException;
import java.io.Writer;

/**
 * Uses a singleton instance of the most recent DICOM version that has relevant changes to
 * the audit message format.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class Current implements SerializationStrategy {

    public static final DICOM2017b INSTANCE = new DICOM2017b();

    public static String toString(AuditMessage auditMessage, boolean pretty) {
        return INSTANCE.marshal(auditMessage, pretty);
    }

    @Override
    public void marshal(AuditMessage auditMessage, Writer writer, boolean pretty) throws IOException {
        INSTANCE.marshal(auditMessage, writer, pretty);
    }

}
