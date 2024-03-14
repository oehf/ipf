/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.ws.cxf;

import java.util.Objects;

import jakarta.activation.DataHandler;
import jakarta.xml.bind.attachment.AttachmentMarshaller;

/**
 * An attachment marshaller implementation which does not read any data
 * from the provided data handlers in order to keep all streams usable.
 *
 * @author Dmytro Rud
 */
public class NonReadingAttachmentMarshaller extends AttachmentMarshaller {
    @Override
    public boolean isXOPPackage() {
        return true;
    }

    @Override
    public String addMtomAttachment(DataHandler data, String elementNamespace, String elementLocalName) {
        return attachmentDescription(data.getName(), null, data.getContentType());
    }

    @Override
    public String addMtomAttachment(byte[] data, int offset, int length, String mimeType,
                                    String elementNamespace, String elementLocalName)
    {
        var size = Integer.toString(Math.min(length, data.length - offset));
        return attachmentDescription(null, size, mimeType);
    }

    @Override
    public String addSwaRefAttachment(DataHandler data) {
        return attachmentDescription(data.getName(), null, data.getContentType());
    }

    private static String attachmentDescription(String name, String size, String contentType) {
        return "Attachment: name='" + valueOrUnknown(name) +
                "', size='" + valueOrUnknown(size) +
                "', content type='" + valueOrUnknown(contentType) + '\'';
    }

    private static String valueOrUnknown(String value) {
        return Objects.toString(value, "[unknown]");
    }

}
