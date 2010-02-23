/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.tutorial.imagebin;

import org.openehealth.ipf.commons.lbs.store.DiskStore;
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jws.WebService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

@WebService(portName = "ImageBin", serviceName = "ImageBinService", 
        targetNamespace = "http://tutorial.openehealth.org/imagebin/", 
        endpointInterface = "org.openehealth.tutorial.imagebin.ImageBin",
        wsdlLocation = "wsdl/imagebin.wsdl")
public class ImageBinImpl implements ImageBin {
    // This is the store where we save our uploaded images
    private final LargeBinaryStore store;

    // Create a store located at a specific path on disk
    public ImageBinImpl(String storeLocation) {
        store = new DiskStore(storeLocation);
    }

    @Override
    public DataHandler download(final String handle) {
        // Create a data handler and source that retrieve the input stream from the store
        return new DataHandler(new DataSource() {
            @Override
            public String getContentType() {
                return "application/octet-stream";
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return store.getInputStream(URI.create(handle));
            }

            @Override
            public String getName() {
                return "image";
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                throw new UnsupportedOperationException();
            }           
        });
    }

    @Override
    public String upload(DataHandler imageData) {
        // Use the input stream in the handler to add it to the store
        try {
            InputStream inputStream = imageData.getInputStream();
            URI resourceUri = store.add(inputStream);
            inputStream.close();
            return resourceUri.toString();
        }
        catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
