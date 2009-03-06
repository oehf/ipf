package org.openehealth.tutorial.imagebin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.openehealth.ipf.commons.lbs.store.DiskStore;
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;
import org.openehealth.tutorial.imagebin.ImageBin;

import javax.jws.WebService;

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

    public DataHandler download(final String handle) {
        // Create a data handler and source that retrieve the input stream from the store
        return new DataHandler(new DataSource() {
            public String getContentType() {
                return "application/octet-stream";
            }

            public InputStream getInputStream() throws IOException {
                return store.getInputStream(URI.create(handle));
            }

            public String getName() {
                return "image";
            }

            public OutputStream getOutputStream() throws IOException {
                throw new UnsupportedOperationException();
            }           
        });
    }

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
