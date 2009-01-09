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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.ws.Holder;

import org.apache.camel.Message;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.cxf.message.MessageContentsList;
import org.openehealth.ipf.commons.lbs.attachment.AttachmentDataSource;
import org.openehealth.ipf.commons.lbs.attachment.AttachmentFactory;

/**
 * A handler for attachments contained in a CXF Soap POJO message.
 * <p>
 * This handler should be used for CXF endpoints using the data format 
 * <code>POJO</code>. Can be used with SwA and MTOM.
 * @author Jens Riemschneider
 */
class CxfPojoAttachmentHandler implements AttachmentHandler {
    private static final String ATTACHMENT_ID_PARAM_PREFIX = "org.openehealth.ipf.platform.camel.lbs.cxf.CxfPojoAttachmentHandler.Param.";
    
    private AttachmentFactory attachmentFactory;

    /**
     * Constructs the handler
     * @param attachmentFactory
     *          the factory for attachments that are extracted by this handler
     */    
    public CxfPojoAttachmentHandler(AttachmentFactory attachmentFactory) {
        notNull(attachmentFactory, "attachmentFactory cannot be null");
        this.attachmentFactory = attachmentFactory;
    }       

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.process.AttachmentExtractionHandler#handle(org.apache.camel.Message)
     */
    @Override
    public Collection<AttachmentDataSource> extract(String unitOfWorkId, Message message) throws Exception {
    	String subUnit = getSubUnit(unitOfWorkId);
        return extractFromParams(subUnit, message, getParams(message));
    }

    private String getSubUnit(String unitOfWorkId) {
		return unitOfWorkId + ".cxf";
	}

	private List<Object> getParams(Message message) {
        try {
            MessageContentsList params = message.getBody(MessageContentsList.class);
            if (params == null) {
                return Collections.emptyList();
            }
            return params;            
        }
        catch (NoTypeConversionAvailableException e) {
            // This is ok. This message is not intended to be processed by this handler
            // TODO: Find a way to do this without exception handling
        }
        return Collections.emptyList();
    }

    private Collection<AttachmentDataSource> extractFromParams(String subUnit, Message message, List<Object> params) throws IOException {
        List<AttachmentDataSource> attachments = new ArrayList<AttachmentDataSource>(); 
        for (int idx = 0; idx < params.size(); ++idx) {
            Object param = params.get(idx);
            if (param instanceof DataHandler) {
                DataHandler dataHandler = (DataHandler) param;
                AttachmentDataSource dataSource = extractFromDataHandler(subUnit, dataHandler, idx);
                attachments.add(dataSource);
                params.set(idx, new DataHandler(dataSource));
            }
            else if (param instanceof Holder) {
                Holder holder = (Holder) param;
                if (holder.value instanceof DataHandler) {
                    DataHandler dataHandler = (DataHandler) holder.value;
                    AttachmentDataSource dataSource = extractFromDataHandler(subUnit, dataHandler, idx);
                    attachments.add(dataSource);
                    holder.value = new DataHandler(dataSource);
                }
            }
        }
        return attachments;
    }

    private AttachmentDataSource extractFromDataHandler(String subUnit, DataHandler handler, int paramIdx) throws IOException {
        InputStream inputStream = handler.getInputStream();
        try {
            String contentType = handler.getContentType();
            String id = ATTACHMENT_ID_PARAM_PREFIX + paramIdx;
            return attachmentFactory.createAttachment(subUnit, contentType, null, id, inputStream);
        }
        finally {
            inputStream.close();
        }
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.process.AttachmentHandler#integrate(org.apache.camel.Message, java.util.Map)
     */
    @Override
    public void integrate(Message message, Collection<AttachmentDataSource> attachments) {
        // Does nothing because attachments are already integrated
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.process.AttachmentHandler#cleanUp(java.lang.String, org.apache.camel.Message)
     */
    @Override
    public void cleanUp(String unitOfWorkId, Message message) {
    	String subUnit = getSubUnit(unitOfWorkId);
        Collection<AttachmentDataSource> attachments = attachmentFactory.getAttachments(subUnit);
        Collection<AttachmentDataSource> requiredAttachments = getRequiredAttachments(message);
        attachments.removeAll(requiredAttachments);
        
        for (AttachmentDataSource attachment : attachments) {                    
            attachmentFactory.deleteAttachment(subUnit, attachment);
        }
        
        for (AttachmentDataSource attachment : requiredAttachments) {                    
            attachmentFactory.deleteAttachmentDelayed(subUnit, attachment);
        }
    }    
    
    private Collection<AttachmentDataSource> getRequiredAttachments(Message message) {
        List<AttachmentDataSource> attachments = new ArrayList<AttachmentDataSource>(); 
        List<Object> params = getParams(message);
        for (Object param : params) {
            AttachmentDataSource attachment = getAttachment(param);
            if (attachment != null) {
                attachments.add(attachment);
            }
        }
        return attachments;
    }

    private AttachmentDataSource getAttachment(Object param) {
        if (param instanceof DataHandler) {
            return getAttachment((DataHandler) param);
        }
        else if (param instanceof Holder) {
            Holder holder = (Holder) param;
            if (holder.value instanceof DataHandler) {
                return getAttachment((DataHandler) holder.value);
            }
        }
        return null;
    }

    private AttachmentDataSource getAttachment(DataHandler handler) {
        DataSource dataSource = handler.getDataSource();
        if (dataSource instanceof AttachmentDataSource) {
            return (AttachmentDataSource) dataSource;
        }
        return null;
    }
}
