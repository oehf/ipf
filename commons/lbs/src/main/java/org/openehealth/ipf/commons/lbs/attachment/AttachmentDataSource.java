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
package org.openehealth.ipf.commons.lbs.attachment;

import static org.apache.commons.lang.Validate.notNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.activation.DataSource;

/**
 * Wrapper class for a data source that is used as an attachment.
 * <p>
 * This class mainly delegates to the data source that it wraps. This wrapper
 * adds specific attachment related support if the original data source is not 
 * an {@link AttachmentCompatibleDataSource}.
 * <p> 
 * The main purpose of this class is to abstract from the different data sources 
 * that can be contained in the Camel attachments. 
 * @author Jens Riemschneider
 */
public class AttachmentDataSource implements AttachmentCompatibleDataSource {
    private final AttachmentCompatibleDataSource dataSource;
    private final String id;

    /**
     * Constructs the data source by wrapping an existing one.
     * <p>
     * Wrapping is performed in the following way:
     * <ul>
     *  <li> If the data source is already an {@code AttachmentDataSource} the
     *  new {@code AttachmentDataSource} wraps the data source contained in the
     *  old one
     *  <li> If the data source is an {@link AttachmentCompatibleDataSource} the
     *  data source is wrapped without adding additional support
     *  <li> If the data source is any other {@link DataSource} it is wrapped
     *  by adding support for an attachments as defined by the 
     *  {@link AttachmentCompatibleDataSource}
     * </ul>
     * @param id
     *          the id of the attachment
     * @param dataSource
     *          the data source to wrap containing the attachment data
     */
    public AttachmentDataSource(String id, DataSource dataSource) {
        notNull(id, "id cannot be null");
        notNull(dataSource, "dataSource cannot be null");
        
        if (dataSource instanceof AttachmentDataSource) {
            // No need to wrap a wrapper
            this.dataSource = ((AttachmentDataSource)dataSource).getWrappedDataSource();
        }
        else if (dataSource instanceof AttachmentCompatibleDataSource) {
            this.dataSource = (AttachmentCompatibleDataSource)dataSource;
        }
        else {
            this.dataSource = new AttachmentCompatibleDataSourceAdapter(dataSource);
        }
        
        this.id = id;
    }

    /** 
     * @return id of the attachment
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.attachment.AttachmentCompatibleDataSource#getResourceUri()
     */
    @Override
    public URI getResourceUri() {
        return dataSource.getResourceUri();
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.attachment.AttachmentCompatibleDataSource#getContentLength()
     */
    @Override
    public long getContentLength() throws IOException {
        return dataSource.getContentLength();
    }

    /* (non-Javadoc)
     * @see javax.activation.DataSource#getContentType()
     */
    @Override
    public String getContentType() {
        return dataSource.getContentType(); 
    }

    /* (non-Javadoc)
     * @see javax.activation.DataSource#getInputStream()
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return dataSource.getInputStream();
    }

    /* (non-Javadoc)
     * @see javax.activation.DataSource#getName()
     */
    @Override
    public String getName() {
        return dataSource.getName();
    }

    /* (non-Javadoc)
     * @see javax.activation.DataSource#getOutputStream()
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        return dataSource.getOutputStream();
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.lbs.attachment.AttachmentCompatibleDataSource#deleteAfterNextUsage()
     */
    @Override
    public void deleteAfterNextUsage() {
        dataSource.deleteAfterNextUsage();
    }
    
    public void delete() {
        dataSource.delete();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{%1$s: id=%2$s, dataSource=%3$s}", getClass()
                .getSimpleName(), id, dataSource);
    }

    private AttachmentCompatibleDataSource getWrappedDataSource() {
        return dataSource;
    }
    
    /**
     * Adapter class for data sources that do not directly support the overall 
     * requirements of an attachment (i.e. {@link AttachmentCompatibleDataSource}).
     * <p>
     * This class adds resource URI and content length support to the data
     * source. 
     * <p>
     * Note that this class does not calculate the length of the content in 
     * advance. It will only do this if the attachment requires this information.
     * The length is also only calculated once. 
     */
    private final class AttachmentCompatibleDataSourceAdapter implements AttachmentCompatibleDataSource {
        private final DataSource dataSource;
        private Long cachedContentLength;
        
        public AttachmentCompatibleDataSourceAdapter(DataSource dataSource) {
            notNull(dataSource, "dataSource cannot be null");
            this.dataSource = dataSource;
        }

        @Override
        public long getContentLength() throws IOException {
            if (cachedContentLength == null) {
                cachedContentLength =  determineContentLength(dataSource);
            }
            return cachedContentLength;
        }

        @Override
        public URI getResourceUri() {
            return null;
        }

        @Override
        public String getContentType() {
            return dataSource.getContentType();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return dataSource.getInputStream();
        }

        @Override
        public String getName() {
            return dataSource.getName();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return dataSource.getOutputStream();
        }

        private long determineContentLength(DataSource dataSource) throws IOException {        
            InputStream inputStream = new BufferedInputStream(dataSource.getInputStream());
            try {
                byte[] buffer = new byte[4096];
                long totalSize = 0;
                boolean done = false;
                do {
                    long bytesRead = inputStream.read(buffer);
                    if (bytesRead >= 0) {  
                        totalSize += bytesRead;
                    }
                    else {
                        done = true;
                    }
                } while (!done);
                return totalSize;
            }
            finally {
                inputStream.close();
            }
        }

        @Override
        public void deleteAfterNextUsage() {}

        @Override
        public void delete() {}
    }
}
