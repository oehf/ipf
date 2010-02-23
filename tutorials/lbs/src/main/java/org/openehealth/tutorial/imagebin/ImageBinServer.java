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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;
import java.io.File;

public class ImageBinServer {
    private static final Log log = LogFactory.getLog(ImageBinServer.class);
    
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
