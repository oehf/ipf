package org.openehealth.tutorial;

import static org.junit.Assert.*;

import java.io.File;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.openehealth.tutorial.imagebin.ImageBinServer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "/context.xml" })
public class SampleRouteTest {
    private ImageBinServer server;
    private HttpClient client;

    // Setup that is run before the application context is loaded
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // There are two stores used in this test. The one from the webservice
        // that contains the image repository and the the one from the route
        // that store the image while they are uploaded  
        
        // Make sure any previously created stores are removed
        File storeLocation = new File("target/store");
        FileUtils.deleteDirectory(storeLocation);

        File routeStoreLocation = new File("target/tempstore");
        FileUtils.deleteDirectory(routeStoreLocation);
        
    }

    @Before
    public void setUp() throws Exception {
        // Start the CXF webservice
        server = new ImageBinServer();
        server.start();

        // Create the HTTP client
        client = new HttpClient();
    }
    
    @After
    public void tearDown() {
        server.stop();
    }

    @Ignore @Test
    public void testUploadAndDownload() throws Exception {
        // Create a post request containing a "fake" image
        PostMethod post = new PostMethod("http://localhost:8412/imagebin");
        StringRequestEntity requestEntity = 
            new StringRequestEntity("TestImage", "application/octet-stream", null);     
        post.setRequestEntity(requestEntity);
        
        // Call the HTTP endpoint and trigger the upload part of the route
        assertEquals(200, client.executeMethod(post));
        String handle = post.getResponseBodyAsString();
        post.releaseConnection();
        
        // Call the HTTP endpoint and trigger the download part of the route
        GetMethod get = new GetMethod("http://localhost:8412/imagebin");
        get.setQueryString("handle=" + handle);
        assertEquals(200, client.executeMethod(get));      
        String imageAsString = get.getResponseBodyAsString();
        get.releaseConnection();

        // Check the download result
        assertEquals("TestImage", imageAsString);
    }
}
