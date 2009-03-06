package org.openehealth.tutorial.imagebin;

import java.io.File;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageBinServer {
    private static Log log = LogFactory.getLog(ImageBinServer.class);
    
    private Endpoint imageBinEndpoint;    

    public void start() {
        File directory = new File("target/store");
        directory.mkdir();
        
        log.debug("Starting ImageBin Server");
        
        // Publish the service
        Object imageBin = new ImageBinImpl(directory.getAbsolutePath());
        String address = "http://localhost:8413/ImageBin/ImageBinPort";
        imageBinEndpoint = Endpoint.publish(address, imageBin);
        
        // Enable MTOM attachments
        SOAPBinding binding = (SOAPBinding) imageBinEndpoint.getBinding();
        binding.setMTOMEnabled(true);
        
        log.debug("ImageBin ready");
    }
    
    public void stop() {        
        log.debug("ImageBin exiting");        
        imageBinEndpoint.stop();
    }
}
