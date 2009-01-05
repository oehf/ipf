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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * Provides access to a storage area for large binaries.
 * <p>
 * Implementations of this interface must be thread safe!
 * @author Jens Riemschneider
 */
public interface LargeBinaryStore {
    /**
     * Adds a binary defined as a byte array to the store.
     * <p>
     * The binary allocates a resource in the store that must be freed by 
     * calling {@link #delete} or {@link #deleteAll()}.
     * @param binary
     *          byte array representing the binary to add
     * @return resource {@code URI} used to access the binary within the store
     * @throws ResourceIOException
     *          if the store could not write the resource to its internal 
     *          storage (e.g. hard disk or database)
     */
    URI add(byte[] binary);

    /**
     * Adds a binary defined as a stream to the store.
     * <p>
     * The binary allocates a resource in the store that must be freed by 
     * calling {@link #delete} or {@link #deleteAll()}.
     * @param binary
     *          stream representing the binary to add
     * @return resource {@code URI} used to access the binary within the store
     * @throws ResourceIOException
     *          if the store could not write the resource to its internal 
     *          storage (e.g. hard disk or database) or if the input stream
     *          could not be read
     */
    URI add(InputStream binary);

    /**
     * Adds an empty binary.
     * <p>
     * This method is used to have more control about the streaming of the
     * actual content. {@link #getOutputStream(URI)} is used to stream the
     * content into the resource.
     * @return resource {@code URI} used to access the binary within the store
     * @throws ResourceIOException
     *          if the store could not create the resource in its internal 
     *          storage (e.g. hard disk or database)
     */
    URI add();
    
    /**
     * Retrieves a binary from the store as a byte array.
     * <p>
     * This method should only be used if sufficient memory storage is available. 
     * Use {@link #getInputStream(URI)} for larger binaries.
     * @param resourceUri
     *          the resource {@code URI} returned by {@link #add}
     * @return byte array containing the binary
     * @throws ResourceNotFoundException
     *          if the resource {@code URI} was not found in the store
     * @throws ResourceIOException
     *          if the store could not read the resource from its internal 
     *          storage (e.g. hard disk or database)
     */
    byte[] getByteArray(URI resourceUri);

    /**
     * Retrieves a binary from the store as a stream.
     * <p>
     * Implementations of this method are required to keep memory usage as low
     * as possible, especially for larger binaries. This method should not 
     * read the whole binary into memory.
     * @param resourceUri
     *          the resource {@code URI} returned by {@link #add}
     * @return stream to the binary contained in the store. This stream must
     *          be closed to release resources allocated with the stream.
     * @throws ResourceNotFoundException
     *          if the resource {@code URI} was not found in the store
     * @throws ResourceIOException
     *          if the store could not read the resource from its internal 
     *          storage (e.g. hard disk or database)
     */
    InputStream getInputStream(URI resourceUri);

    /**
     * Deletes a binary from the store.
     * <p>
     * This method frees the resource allocated by the binary via a call to 
     * {@link #add}.
     * @param resourceUri
     *          the resource {@code URI} returned by {@link #add}
     * @throws ResourceNotFoundException
     *          if the resource {@code URI} was not found in the store
     * @throws ResourceIOException
     *          if the store could not remove the resource from its internal 
     *          storage (e.g. hard disk or database)
     */
    void delete(URI resourceUri);
    
    /**
     * Deletes all binaries from the store.
     * <p>
     * This method frees all resources allocated by the binaries currently in 
     * the store.
     * @throws ResourceIOException
     *          if the store could not remove a resource from its internal 
     *          storage (e.g. hard disk or database)
     */
    void deleteAll();

    /**
     * Determines if a resource is contained in the store.
     * @param resourceUri
     *          the resource {@code URI}
     * @return {@code true} if the {@code URI} was found within the store,  
     *          otherwise {@code false}.
     * @throws ResourceIOException
     *          if the store could not access the resource in its internal 
     *          storage (e.g. hard disk or database)
     */
    boolean contains(URI resourceUri);

    /**
     * Determines the size of a resource contained in the store.
     * @param resourceUri
     *          the resource {@code URI} returned by {@link #add}
     * @return the size of the resource in the store (in bytes)
     * @throws ResourceNotFoundException
     *          if the resource {@code URI} was not found in the store
     * @throws ResourceIOException
     *          if the store could not access the resource in its internal 
     *          storage (e.g. hard disk or database)
     */
    long getSize(URI resourceUri);

    /**
     * Returns an output stream that is used to fill the content of the
     * resource.
     * @param resourceUri
     *          the resource {@code URI} returned by {@link #add}
     * @throws ResourceNotFoundException
     *          if the resource {@code URI} was not found in the store
     * @throws ResourceIOException
     *          if the store could not access the resource in its internal 
     *          storage (e.g. hard disk or database)
     */
    OutputStream getOutputStream(URI resourceUri);
}
