package org.openehealth.tutorial.imagebin;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.ws.BindingProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.tutorial.imagebin.ImageBin;
import org.openehealth.tutorial.imagebin.ImageBinServer;
import org.openehealth.tutorial.imagebin.ImageBinService;

public class ImageBinServerTest {
    private ImageBinServer imageBinServer;

    @Before
    public void setUp() throws Exception {
        // Make sure a previously created store is removed
        File storeLocation = new File("target/store");
        FileUtils.deleteDirectory(storeLocation);
        
        // Start the CXF webservice
        imageBinServer = new ImageBinServer();
        imageBinServer.start();
    }
    
    @After
    public void tearDown() throws Exception {
        imageBinServer.stop();
    }
    
    @Test
    public void testUpAndDownload() throws Exception {
        // Create a client interface to the CXF webservice
        URL wsdlUrl = getClass().getClassLoader().getResource("wsdl/imagebin.wsdl");
        ImageBinService service = new ImageBinService(wsdlUrl);
        ImageBin imageBin = service.getImageBin();
        BindingProvider provider = (BindingProvider)imageBin;
        provider.getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8413/ImageBin/ImageBinPort");

        // Image data doesn't need to be a real image
        byte[] imageData = "TestImage".getBytes();

        // Call the service to upload the image
        ByteArrayDataSource dataSource = new ByteArrayDataSource(imageData, "application/octet-stream");
        DataHandler myImage = new DataHandler(dataSource);
        String handle = imageBin.upload(myImage);
        
        // Download the image again
        DataHandler downloadedImage = imageBin.download(handle);
        
        // And check if we received the image data
        InputStream inputStream = downloadedImage.getInputStream();
        assertTrue("Image data is not equal", 
                IOUtils.contentEquals(myImage.getInputStream(), inputStream));
        inputStream.close();
    }
}
