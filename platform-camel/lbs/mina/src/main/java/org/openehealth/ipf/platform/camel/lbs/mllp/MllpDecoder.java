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
package org.openehealth.ipf.platform.camel.lbs.mllp;

import static org.apache.commons.lang.Validate.notNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.camel.util.UuidGenerator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.openehealth.ipf.commons.lbs.attachment.AttachmentDataSource;
import org.openehealth.ipf.commons.lbs.attachment.AttachmentFactory;
import org.openehealth.ipf.platform.camel.lbs.mllp.MllpMessagePart.MllpMessageExtractionState;

/**
 * Decoder implementation that delimits messages via the MLLP encoding and stores
 * it into a data source.
 * @author Jens Riemschneider
 */
public class MllpDecoder extends CumulativeProtocolDecoder {
    private static final String SESSION_CONTENT = MllpDecoder.class.getName() + ".SessionContent";
    
    private AttachmentFactory attachmentFactory;
    
    private static final Log log = LogFactory.getLog(MllpDecoder.class);    

    /**
     * Construct the decoder
     * @param attachmentFactory
     *          the factory to create messages
     */
    public MllpDecoder(AttachmentFactory attachmentFactory) {
        notNull(attachmentFactory, "attachmentFactory cannot be null");
        this.attachmentFactory = attachmentFactory;
    }
    
    /* (non-Javadoc)
     * @see org.apache.mina.filter.codec.CumulativeProtocolDecoder#doDecode(org.apache.mina.common.IoSession, org.apache.mina.common.ByteBuffer, org.apache.mina.filter.codec.ProtocolDecoderOutput)
     */
    @Override
    protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
        notNull(out, "out cannot be null");
        notNull(in, "in cannot be null");
        notNull(session, "session cannot be null");
        
        SessionContent sessionContent = getSessionContent(session);        
        return doDecode(sessionContent, in, out);
    }

    boolean doDecode(SessionContent sessionContent, ByteBuffer in, ProtocolDecoderOutput out) throws IOException {
        AttachmentDataSource message = sessionContent.getMessage();
        MllpMessageExtractionState state = sessionContent.getExtractionState();
        OutputStream outputStream = sessionContent.getOutputStream(message);
        
        MllpMessagePart part = MllpMessagePart.extractMessage(state, in);
        
        copy(in, part, outputStream);
        
        if (part.isComplete()) {
            outputStream.close();
            try {
                out.write(message);
                log.debug("decoded message: " + message);
            }
            catch (RuntimeException e) {
                sessionContent.cleanUpOpenMessage();
                throw e;
            }
            finally {
                sessionContent.resetMessage();
            }
            return true;
        }

        return false;
    }
    
    /* (non-Javadoc)
     * @see org.apache.mina.filter.codec.ProtocolDecoderAdapter#finishDecode(org.apache.mina.common.IoSession, org.apache.mina.filter.codec.ProtocolDecoderOutput)
     */
    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
        notNull(out, "out cannot be null");
        notNull(session, "session cannot be null");
        
        super.finishDecode(session, out);
        SessionContent sessionContent = (SessionContent) session.removeAttribute(SESSION_CONTENT);
        finishDecode(sessionContent);
    }

    void finishDecode(SessionContent sessionContent) throws IOException {
        if (sessionContent != null) {
            sessionContent.cleanUpOpenMessage();
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

    private SessionContent getSessionContent(IoSession session) throws IOException {
        SessionContent sessionContent = (SessionContent) session.getAttribute(SESSION_CONTENT);
        
        if (sessionContent == null) {
            sessionContent = new SessionContent(attachmentFactory);
            session.setAttribute(SESSION_CONTENT, sessionContent);
        }
        
        return sessionContent;
    }
    
    private void copy(ByteBuffer in, MllpMessagePart message, OutputStream outputStream) throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(message.asByteArray());
        try {
            IOUtils.copy(input, outputStream);
        }
        finally {
            input.close();
        }
    }
    
    static class SessionContent {        
        private AttachmentFactory attachmentFactory;
        private OutputStream outputStream;
        private MllpMessageExtractionState state;
        private AttachmentDataSource message;
        private String unitOfWorkId;
        
        public SessionContent(AttachmentFactory attachmentFactory) {
            notNull(attachmentFactory, "attachmentFactory cannot be null");
            this.attachmentFactory = attachmentFactory;
            this.unitOfWorkId = new UuidGenerator().generateId() + ".mllp";
        }
        
        public void resetMessage() {
            message = null;
            outputStream = null;
        }

        public void cleanUpOpenMessage() throws IOException {
            if (outputStream != null) {
                outputStream.close();
            }
            
            List<AttachmentDataSource> createdAttachments = attachmentFactory.getAttachments(unitOfWorkId);
            for (AttachmentDataSource attachment : createdAttachments) {
                attachmentFactory.deleteAttachment(unitOfWorkId, attachment);
            }
        }
        
        public MllpMessageExtractionState getExtractionState() {
            if (state == null) {
                state = new MllpMessageExtractionState();
            }
            return state;
        }

        public AttachmentDataSource getMessage() throws IOException {
            if (message == null) {
                message = attachmentFactory.createAttachment(unitOfWorkId);
            }            
            return message;
        }
        
        public OutputStream getOutputStream(AttachmentDataSource message) throws IOException {
            if (outputStream == null) {
                outputStream = message.getOutputStream();
            }            
            return outputStream;
        }
    }
}
