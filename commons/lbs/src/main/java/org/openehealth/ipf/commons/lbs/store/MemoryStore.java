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
package org.openehealth.ipf.commons.lbs.store;

import static org.apache.commons.lang.Validate.notNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.lbs.store.ResourceIOException;
import org.openehealth.ipf.commons.lbs.store.ResourceNotFoundException;

/**
 * Memory-based implementation of the large binary store interface.
 * <p>
 * This implementation can be used in tests to avoid writing to disk or to a 
 * database. The store is not suited for production use because it keeps all 
 * binaries in memory. 
 * <p>
 * This class is thread-safe.
 * @author Jens Riemschneider
 */
public class MemoryStore implements LargeBinaryStore {
    private static final String STORE_URI_SCHEME = "mockstore";
    
    private AtomicLong currentResourceCounter = new AtomicLong();
    private ConcurrentMap<URI, byte[]> resources = new ConcurrentHashMap<URI, byte[]>();

    private static final Log log = LogFactory.getLog(MemoryStore.class);
    
    /**
     * Constructs the memory store 
     */
    public MemoryStore() {
        log.debug("created: " + this);
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.lbs.LargeBinaryStore#add(byte[])
     */
    @Override
    public URI add(byte[] binary) {
        notNull(binary, "binary cannot be null");
        return addResource(binary);
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.lbs.LargeBinaryStore#add(java.io.InputStream)
     */
    @Override
    public URI add(InputStream binary) {
        notNull(binary, "binary cannot be null");
        try {
            return add(IOUtils.toByteArray(binary));
        }
        catch (IOException e) {
            throw new ResourceIOException(e);
        }
    }


    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.lbs.LargeBinaryStore#add()
     */
    @Override
    public URI add() {
        return add(new byte[] {});
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.lbs.LargeBinaryStore#contains(java.net.URI)
     */
    @Override
    public boolean contains(URI resourceUri) {
        notNull(resourceUri, "resourceUri cannot be null");
        return resources.containsKey(resourceUri);
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.lbs.LargeBinaryStore#delete(java.net.URI)
     */
    @Override
    public void delete(URI resourceUri) {
        notNull(resourceUri, "resourceUri cannot be null");
        if (!contains(resourceUri)) {
            throw new ResourceNotFoundException(resourceUri);
        }
        resources.remove(resourceUri);
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.lbs.LargeBinaryStore#deleteAll()
     */
    @Override
    public void deleteAll() {
        resources.clear();
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.lbs.LargeBinaryStore#getByteArray(java.net.URI)
     */
    @Override
    public byte[] getByteArray(URI resourceUri) {
        notNull(resourceUri, "resourceUri cannot be null");
        if (!contains(resourceUri)) {
            throw new ResourceNotFoundException(resourceUri);
        }
        return resources.get(resourceUri);
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.lbs.LargeBinaryStore#getStream(java.net.URI)
     */
    @Override
    public InputStream getInputStream(URI resourceUri) {
        return new ByteArrayInputStream(getByteArray(resourceUri)); 
    }
    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.lbs.LargeBinaryStore#getSize(java.net.URI)
     */
    @Override
    public long getSize(URI resourceUri) {
        notNull(resourceUri, "resourceUri cannot be null");
        if (!contains(resourceUri)) {
            throw new ResourceNotFoundException(resourceUri);
        }
        return resources.get(resourceUri).length;
        
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.lbs.LargeBinaryStore#getOutputStream(java.net.URI)
     */
    @Override
    public OutputStream getOutputStream(final URI resourceUri) {
        notNull(resourceUri, "resourceUri cannot be null");
        if (!contains(resourceUri)) {
            throw new ResourceNotFoundException(resourceUri);
        }

        return new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                super.close();
                resources.put(resourceUri, toByteArray());
            }  
        };
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{%1$s: currentResourceCounter=%2$s}",
                getClass().getSimpleName(), currentResourceCounter); 
    }
    
    private URI addResource(byte[] binary) {
        URI uri = createNewUri();        
        resources.put(uri, binary);     // Don't need putIfAbsent here because
                                        // the URI is definitely unused
        
        log.debug("added resource: uri=" + uri);
        return uri;
    }
    
    private URI createNewUri() {
        long resourceIndex = currentResourceCounter.incrementAndGet();
        return URI.create(STORE_URI_SCHEME + "://" + resourceIndex);
    }
}
