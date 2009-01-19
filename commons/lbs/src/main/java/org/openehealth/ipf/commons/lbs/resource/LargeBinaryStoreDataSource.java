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
package org.openehealth.ipf.commons.lbs.resource;

import static org.apache.commons.lang.Validate.notNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;
import org.openehealth.ipf.commons.lbs.store.StoreRegistration;

/**
 * Data source implementation that wraps a resource {@code URI} from a
 * {@link LargeBinaryStore}.
 * <p>
 * This implementation supports read-only usage of the data source as a
 * resource.
 * 
 * @author Jens Riemschneider
 */
final public class LargeBinaryStoreDataSource implements ResourceCompatibleDataSource, Serializable {
    /** Serialization UID */
    private static final long serialVersionUID = 3930847973245118248L;
    
    private final URI resourceUri;
    private final String contentType;
    private final String name;

    private boolean deleteResourceAfterUsage;

    private static final Log log = LogFactory.getLog(LargeBinaryStoreDataSource.class);
    
    /**
     * Constructs the data source
     * @param resourceUri
     *            the {@code URI} of the resource in the store
     * @param contentType
     *            type of the content stored in the resource
     * @param name
     *            name of the resource (can be {@code null})
     */
    public LargeBinaryStoreDataSource(URI resourceUri, String contentType, String name) {
        notNull(contentType, "contentType cannot be null");
        notNull(resourceUri, "resourceUri cannot be null");

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
        InputStream inputStream = getStore().getInputStream(resourceUri);
        if (deleteResourceAfterUsage) {
            inputStream = new AutoRemoveInputStream(inputStream);
        }
        return inputStream;
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.lbs.resource.ResourceCompatibleDataSource#deleteAfterNextUsage()
     */
    @Override
    public void deleteAfterNextUsage() {
        deleteResourceAfterUsage = true;
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.lbs.resource.ResourceCompatibleDataSource#delete()
     */
    @Override
    public void delete() {
        getStore().delete(resourceUri);
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
        return getStore().getOutputStream(resourceUri);
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.resource.ResourceCompatible#getResourceUri()
     */
    @Override
    public URI getResourceUri() {
        return resourceUri;
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.resource.ResourceCompatible#getContentLength()
     */
    @Override
    public long getContentLength() {
        return getStore().getSize(resourceUri);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                    "{%1$s: contentType=%2$s, name=%3$s, resourceUri=%4$s}",
                    getClass().getSimpleName(), contentType, name, resourceUri);
    }
    
    private LargeBinaryStore getStore() {
        return StoreRegistration.getStore(resourceUri);
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
}
