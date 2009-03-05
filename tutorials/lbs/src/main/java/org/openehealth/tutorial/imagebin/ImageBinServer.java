package org.openehealth.tutorial.imagebin;

import java.io.File;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

public class ImageBinServer {
    private Endpoint imageBinEndpoint;

    public void start() {
        File directory = new File("target/store");
        directory.mkdir();
        
        System.out.println("Starting ImageBin Server");
        
        // Publish the service
        Object imageBin = new ImageBinImpl(directory.getAbsolutePath());
        String address = "http://localhost:8413/ImageBin/ImageBinPort";
        imageBinEndpoint = Endpoint.publish(address, imageBin);
        
        // Enable MTOM attachments
        SOAPBinding binding = (SOAPBinding) imageBinEndpoint.getBinding();
        binding.setMTOMEnabled(true);
        
        System.out.println("ImageBin ready...");
    }
    
    public void stop() {        
        System.out.println("ImageBin exiting");        
        imageBinEndpoint.stop();
    }
}
