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
package org.openehealth.ipf.platform.camel.lbs.http.process;

import static org.apache.commons.lang.Validate.notNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Message;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.component.http.HttpMethods;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.io.IOUtils;
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource;
import org.openehealth.ipf.commons.lbs.resource.ResourceFactory;
import org.openehealth.ipf.platform.camel.lbs.core.process.ResourceHandler;

/**
 * A handler for resource contained in an Http message.
 * <p>
 * This handler can extract multipart messages
 * @author Jens Riemschneider
 */
public class HttpResourceHandler implements ResourceHandler {
    private final ResourceFactory resourceFactory;

    /**
     * Constructs the handler
     * @param resourceFactory
     *          the factory for resources that are extracted by this handler
     */    
    public HttpResourceHandler(ResourceFactory resourceFactory) {
        notNull(resourceFactory, "resourceFactory cannot be null");
        this.resourceFactory = resourceFactory;
    }    
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.core.process.ResourceHandler#handle(org.apache.camel.Message)
     */
    @Override
    public Collection<ResourceDataSource> extract(String unitOfWorkId, Message message) throws Exception {
        try {
            HttpServletRequest request = message.getBody(HttpServletRequest.class);
            if (request != null) {
            	String subUnit = getSubUnit(unitOfWorkId);
                ResourceList resources = extract(subUnit, request);
                message.setBody(resources);
                return resources;
            }
        }
        catch (NoTypeConversionAvailableException e) {
            // This is ok. This message is not intended to be processed by this handler
            // TODO: Find a way to do this without exception handling
        }
        return Collections.emptyList();
    }

    private String getSubUnit(String unitOfWorkId) {
		return unitOfWorkId + ".http";
	}

	/* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.core.process.ResourceHandler#integrate(org.apache.camel.Message, java.util.Map)
     */
    @Override
    public void integrate(Message message) throws Exception {
        try {
            ResourceList resourceList = message.getMandatoryBody(ResourceList.class);
            if (resourceList != null) {
                integrate(message, resourceList);
            }
        }
        catch (InvalidPayloadException e) {
            // This is ok. This message is not intended to be processed by this handler
            // TODO: Find a way to do this without exception handling
        }
    }

    private void integrate(Message message, ResourceList resourceList) {
        if (resourceList.size() > 1) {
            integrateToMultipart(message, resourceList);
        }
        else if (resourceList.size() == 1) {
            integrateToSingle(message, resourceList);
        }
    }
    
    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.core.process.ResourceHandler#cleanUp(java.lang.String, org.apache.camel.Message)
     */
    @Override
    public void cleanUp(String unitOfWorkId, Message message, List<ResourceDataSource> requiredResources) {
    	String subUnit = getSubUnit(unitOfWorkId);    	
        Collection<ResourceDataSource> resources = resourceFactory.getResources(subUnit);        
        for (ResourceDataSource resource : resources) {
            if (requiredResources.contains(resource)) {
                resourceFactory.deleteResourceDelayed(subUnit, resource);
            }
            else {
                resourceFactory.deleteResource(subUnit, resource);
            }
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{%1$s: resourceFactory=%2$s}", getClass()
                .getSimpleName(), resourceFactory);
    }
    
    private void integrateToSingle(Message message, ResourceList resourceList) {
        // Note: We don't use InputStreamRequestEntity here. It sounds like the
        //       perfect fit, but it does not close the stream because it thinks
        //       that we can do that later after the request is processed. 
        //       However, we are not in control of the actual sending of the 
        //       request at this point, therefore it is better to get the
        //       input stream from the data source during 
        //       RequestEntity#writeRequest and that is done within
        //       ResourceRequestEntity.
        RequestEntity requestEntity = new ResourceRequestEntity(resourceList.get(0));
        message.setBody(requestEntity);
    }
    
    private static class ResourceRequestEntity implements RequestEntity {
        private final ResourceDataSource resource;
        
        public ResourceRequestEntity(ResourceDataSource resource) {
            this.resource = resource;
        }
        
        @Override
        public long getContentLength() {
            try {
                return resource.getContentLength();
            } catch (IOException e) {
                return -1;
            }
        }

        @Override
        public String getContentType() {
            return resource.getContentType();
        }

        @Override
        public boolean isRepeatable() {
            return false;
        }

        @Override
        public void writeRequest(OutputStream output) throws IOException {
            InputStream input = resource.getInputStream();
            try {
                IOUtils.copy(input, output);
            }
            finally {
                input.close();
                // output is not ours, we don't close it
            }
        }        
    }

    private void integrateToMultipart(Message message, ResourceList resourceList) {
        message.setHeader(Exchange.HTTP_METHOD, HttpMethods.POST);
        
        PostMethod method = new PostMethod();
        Part[] parts = new Part[resourceList.size()];
        
        int idx = 0;
        for (ResourceDataSource resource : resourceList) {
            parts[idx] = new ResourcePart(resource.getId(), resource);
            ++idx;
        }
        message.setBody(new MultipartRequestEntity(parts, method.getParams()));
    }
    
    private static class ResourcePart extends PartBase {
        private final ResourceDataSource resource;

        private static final String DEFAULT_TRANSFER_ENCODING = "binary";
        private static final String DEFAULT_CHARSET = "ISO-8859-1";

        public ResourcePart(String name, ResourceDataSource resource) {
            super(name, resource.getContentType(), 
                    DEFAULT_CHARSET, DEFAULT_TRANSFER_ENCODING);
            
            this.resource = resource;
        }

        @Override
        protected long lengthOfData() throws IOException {
            return resource.getContentLength();
        }

        @Override
        protected void sendData(OutputStream output) throws IOException {
            InputStream input = resource.getInputStream();
            try {
                IOUtils.copy(input, output);
            }
            finally {
                input.close();
            }
        }
    }

    private ResourceList extract(String subUnit, HttpServletRequest request) throws Exception {
        if (ServletFileUpload.isMultipartContent(request)) {            
            return extractFromMultipart(subUnit, request);
        }
        return extractFromSingle(subUnit, request);
    }

    private ResourceList extractFromSingle(String subUnit, HttpServletRequest request) throws Exception {
        ResourceList resources = new ResourceList();
        handleResource(subUnit, resources, 
                request.getContentType(), request.getPathTranslated(), null, request.getInputStream());        
        return resources;
    }

    private ResourceList extractFromMultipart(String subUnit, HttpServletRequest request) throws Exception {
        ResourceList resources = new ResourceList();
        ServletFileUpload upload = new ServletFileUpload(null);
        FileItemIterator iter = upload.getItemIterator(request);
        while (iter.hasNext()) {
            extractFromSinglePart(subUnit, resources, iter);
        }
        
        return resources;
    }

    private void extractFromSinglePart(String subUnit, ResourceList resources, FileItemIterator iter) throws Exception {        
        FileItemStream next = iter.next();
        InputStream inputStream = next.openStream();
        try {
            handleResource(subUnit, resources, 
                    next.getContentType(), next.getName(), next.getFieldName(), inputStream);
        }
        finally {
            inputStream.close();
        }
    }

    private void handleResource(String subUnit, ResourceList resources, 
            String contentType, String name, String id, InputStream inputStream) {
        
        ResourceDataSource resource = 
            resourceFactory.createResource(subUnit, contentType, name, id, inputStream);
        
        resources.add(resource);
    }

    @Override
    public Collection<? extends ResourceDataSource> getRequiredResources(Message message) {
        try {
            return Collections.singletonList(message.getMandatoryBody(ResourceDataSource.class));
        }
        catch (InvalidPayloadException e) {
            // This is ok. This message is not intended to be processed by this handler
            // TODO: Find a way to do this without exception handling
        }
        return Collections.emptyList();
    }

}
