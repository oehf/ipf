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
package org.openehealth.ipf.platform.camel.lbs.process;

import static org.apache.commons.lang.Validate.notNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.Message;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.component.http.HttpMethods;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.io.IOUtils;
import org.openehealth.ipf.commons.lbs.attachment.AttachmentDataSource;
import org.openehealth.ipf.commons.lbs.attachment.AttachmentFactory;

/**
 * A handler for attachments contained in an Http message.
 * <p>
 * This handler can extract multipart messages
 * @author Jens Riemschneider
 */
public class HttpAttachmentHandler implements AttachmentHandler {
    private AttachmentFactory attachmentFactory;

    /**
     * Constructs the handler
     * @param attachmentFactory
     *          the factory for attachments that are extracted by this handler
     */    
    public HttpAttachmentHandler(AttachmentFactory attachmentFactory) {
        notNull(attachmentFactory, "attachmentFactory cannot be null");
        this.attachmentFactory = attachmentFactory;
    }    
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.process.AttachementExtractionHandler#handle(org.apache.camel.Message)
     */
    @Override
    public Collection<AttachmentDataSource> extract(String unitOfWorkId, Message message) throws Exception {
        try {
            HttpServletRequest request = message.getBody(HttpServletRequest.class);
            if (request != null) {
            	String subUnit = getSubUnit(unitOfWorkId);
                Collection<AttachmentDataSource> attachments = extract(subUnit, request);
                message.setBody(attachments.size() == 1 ? attachments.iterator().next() : "");
                return attachments;
            }
        }
        catch (NoTypeConversionAvailableException e) {
            // This is ok. This message is not intended to be processed by this handler
            // TODO: Find a way to do this without exception handling
        }
        return Collections.emptyList();
    }

    private String getSubUnit(String unitOfWorkId) {
		return unitOfWorkId + ".http";
	}

	/* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.process.AttachmentHandler#integrate(org.apache.camel.Message, java.util.Map)
     */
    @Override
    public void integrate(Message message, Collection<AttachmentDataSource> attachments) throws Exception {
        if (attachments.size() > 1) {
            integrateToMultipart(message, attachments);
        }
        else if (attachments.size() == 1) {
            integrateToSingle(message, attachments);
        }
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.process.AttachmentHandler#cleanUp(java.lang.String, org.apache.camel.Message)
     */
    @Override
    public void cleanUp(String unitOfWorkId, Message message, List<AttachmentDataSource> requiredAttachments) {
    	String subUnit = getSubUnit(unitOfWorkId);    	
        Collection<AttachmentDataSource> attachments = attachmentFactory.getAttachments(subUnit);        
        for (AttachmentDataSource attachment : attachments) {
            if (requiredAttachments.contains(attachment)) {
                attachmentFactory.deleteAttachmentDelayed(subUnit, attachment);
            }
            else {
                attachmentFactory.deleteAttachment(subUnit, attachment);
            }
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{%1$s: attachmentFactory=%2$s}", getClass()
                .getSimpleName(), attachmentFactory);
    }
    
    private void integrateToSingle(Message message, Collection<AttachmentDataSource> attachments) throws IOException {
        AttachmentDataSource attachment = attachments.iterator().next();
        
        // Note: We don't use InputStreamRequestEntity here. It sounds like the
        //       perfect fit, but it does not close the stream because it thinks
        //       that we can do that later after the request is processed. 
        //       However, we are not in control of the actual sending of the 
        //       request at this point, therefore it is better to get the
        //       input stream from the data source during 
        //       RequestEntity#writeRequest and that is done within
        //       AttachmentRequestEntity.
        RequestEntity requestEntity = new AttachmentRequestEntity(attachment);
        message.setBody(requestEntity);
    }
    
    private static class AttachmentRequestEntity implements RequestEntity {
        private AttachmentDataSource attachment;
        
        public AttachmentRequestEntity(AttachmentDataSource attachment) {
            this.attachment = attachment;
        }
        
        @Override
        public long getContentLength() {
            try {
                return attachment.getContentLength();
            } catch (IOException e) {
                return -1;
            }
        }

        @Override
        public String getContentType() {
            return attachment.getContentType();
        }

        @Override
        public boolean isRepeatable() {
            return false;
        }

        @Override
        public void writeRequest(OutputStream output) throws IOException {
            InputStream input = attachment.getInputStream();
            try {
                IOUtils.copy(input, output);
            }
            finally {
                input.close();
                // output is not ours, we don't close it
            }
        }        
    }

    private void integrateToMultipart(Message message, Collection<AttachmentDataSource> attachments) {
        message.setHeader(HttpMethods.HTTP_METHOD, HttpMethods.POST);
        
        PostMethod method = new PostMethod();
        Part[] parts = new Part[attachments.size()];
        
        int idx = 0;
        for (AttachmentDataSource attachment : attachments) {            
            parts[idx] = new AttachmentPart(attachment.getId(), attachment);
            ++idx;
        };
        message.setBody(new MultipartRequestEntity(parts, method.getParams()));
    }
    
    private static class AttachmentPart extends PartBase {
        private AttachmentDataSource attachment;

        private static final String DEFAULT_TRANSFER_ENCODING = "binary";
        private static final String DEFAULT_CHARSET = "ISO-8859-1";

        public AttachmentPart(String name, AttachmentDataSource attachment) {
            super(name, attachment.getContentType(), 
                    DEFAULT_CHARSET, DEFAULT_TRANSFER_ENCODING);
            
            this.attachment = attachment;
        }

        @Override
        protected long lengthOfData() throws IOException {
            return attachment.getContentLength();
        }

        @Override
        protected void sendData(OutputStream output) throws IOException {
            InputStream input = attachment.getInputStream();
            try {
                IOUtils.copy(input, output);
            }
            finally {
                input.close();
            }
        }
    }

    private Collection<AttachmentDataSource> extract(String subUnit, HttpServletRequest request) throws Exception {
        if (ServletFileUpload.isMultipartContent(request)) {            
            return extractFromMultipart(subUnit, request);
        }
        return extractFromSingle(subUnit, request);
    }

    private List<AttachmentDataSource> extractFromSingle(String subUnit, HttpServletRequest request) throws Exception {
        List<AttachmentDataSource> attachments = new ArrayList<AttachmentDataSource>();
        handleAttachment(subUnit, attachments, 
                request.getContentType(), request.getPathTranslated(), null, request.getInputStream());        
        return attachments;
    }

    private List<AttachmentDataSource> extractFromMultipart(String subUnit, HttpServletRequest request) throws Exception {
        List<AttachmentDataSource> attachments = new ArrayList<AttachmentDataSource>();
        ServletFileUpload upload = new ServletFileUpload(null);
        FileItemIterator iter = upload.getItemIterator(request);
        while (iter.hasNext()) {
            extractFromSinglePart(subUnit, attachments, iter);
        }
        
        return attachments;
    }

    private void extractFromSinglePart(String subUnit, List<AttachmentDataSource> attachments, FileItemIterator iter) throws Exception {        
        FileItemStream next = iter.next();
        InputStream inputStream = next.openStream();
        try {
            handleAttachment(subUnit, attachments, 
                    next.getContentType(), next.getName(), next.getFieldName(), inputStream);
        }
        finally {
            inputStream.close();
        }
    }

    private void handleAttachment(String subUnit, List<AttachmentDataSource> attachments, 
            String contentType, String name, String id, InputStream inputStream) throws IOException {
        
        AttachmentDataSource attachment = 
            attachmentFactory.createAttachment(subUnit, contentType, name, id, inputStream);
        
        attachments.add(attachment);
    }

    @Override
    public Collection<? extends AttachmentDataSource> getRequiredAttachments(Message message) {
        try {
            return Collections.singletonList(message.getBody(AttachmentDataSource.class));
        }
        catch (NoTypeConversionAvailableException e) {
            // This is ok. This message is not intended to be processed by this handler
            // TODO: Find a way to do this without exception handling
        }
        return Collections.emptyList();
    }

}
