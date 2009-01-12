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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;

/**
 * Data source implementation that wraps a resource {@code URI} from a
 * {@link LargeBinaryStore}.
 * <p>
 * This implementation supports read-only usage of the data source as an
 * attachment.
 * 
 * @author Jens Riemschneider
 */
final public class LargeBinaryStoreDataSource implements AttachmentCompatibleDataSource {
    private final LargeBinaryStore store;
    private final URI resourceUri;
    private final String contentType;
    private final String name;

    private boolean deleteResourceAfterUsage;

    private static final Log log = LogFactory.getLog(LargeBinaryStoreDataSource.class);
    
    /**
     * Constructs the data source.
     * 
     * @param store
     *            the store that manages the resource
     * @param resourceUri
     *            the {@code URI} of the resource in the store
     * @param contentType
     *            type of the content stored in the resource
     * @param name
     *            name of the resource (can be {@code null})
     */
    public LargeBinaryStoreDataSource(LargeBinaryStore store, URI resourceUri,
            String contentType, String name) {

        notNull(contentType, "contentType cannot be null");
        notNull(resourceUri, "resourceUri cannot be null");
        notNull(store, "store cannot be null");

        this.store = store;
        this.resourceUri = resourceUri;
        this.contentType = contentType;
        this.name = name;
    }

    /* (non-Javadoc)
     * @see javax.activation.DataSource#getContentType()
     */
    @Override
    public String getContentType() {
        return contentType;
    }

    /* (non-Javadoc)
     * @see javax.activation.DataSource#getInputStream()
     */
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = store.getInputStream(resourceUri);
        if (deleteResourceAfterUsage) {
            inputStream = new AutoRemoveInputStream(inputStream);
        }
        return inputStream;
    }

    private class AutoRemoveInputStream extends FilterInputStream {
        protected AutoRemoveInputStream(InputStream in) {
            super(in);
        }
        
        @Override
        public void close() throws IOException {
            super.close();
            log.debug("Deleting resource after closing InputStream: " + getResourceUri());
            delete();
        }
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.lbs.attachment.AttachmentCompatibleDataSource#deleteAfterNextUsage()
     */
    @Override
    public void deleteAfterNextUsage() {
        deleteResourceAfterUsage = true;
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.lbs.attachment.AttachmentCompatibleDataSource#delete()
     */
    @Override
    public void delete() {
        store.delete(resourceUri);
    }
    
    /* (non-Javadoc)
     * @see javax.activation.DataSource#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see javax.activation.DataSource#getOutputStream()
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        return store.getOutputStream(resourceUri);
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.attachment.ResourceCompatible#getResourceUri()
     */
    @Override
    public URI getResourceUri() {
        return resourceUri;
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.attachment.ResourceCompatible#getContentLength()
     */
    @Override
    public long getContentLength() {
        return store.getSize(resourceUri);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                    "{%1$s: contentType=%2$s, name=%3$s, resourceUri=%4$s, store=%5$s}",
                    getClass().getSimpleName(), contentType, name,
                    resourceUri, store);
    }
}