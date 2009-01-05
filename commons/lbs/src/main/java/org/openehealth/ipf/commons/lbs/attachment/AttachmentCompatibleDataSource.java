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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.activation.DataSource;

/**
 * Represents a data source that is compatible with the attachment requirements.
 * <p>
 * Attachments require functionality in addition to that supported by a standard
 * {@link DataSource}. This includes:
 * <ul>
 *  <li> Support for retrieving a resource {@code URI} that represents a storage 
 *    location of the resource encapsulated by the attachment
 *  <li> Support for retrieving the content length
 * </ul>
 *  
 * @author Jens Riemschneider
 */
public interface AttachmentCompatibleDataSource extends DataSource {

    /** 
     * @return resource URI of the attachment, or {@code null} if the URI cannot
     *          be determined
     */
    URI getResourceUri();

    /**
     * @return length of the data contained in the data handler
     * @throws IOException
     *          if determining the content size failed
     */
    long getContentLength() throws IOException;
    
    /**
     * Signals the end of the life cycle of the resource contained in this data source.
     * <p>
     * A call to this method ensures that the underlying resource is being deleted
     * after it was read via {@link #getInputStream()} and the stream was closed via
     * {@link InputStream#close()}.
     */
    void deleteAfterNextUsage();

    /**
     * Immediately delete the underlying resource 
     */
    void delete();
}
