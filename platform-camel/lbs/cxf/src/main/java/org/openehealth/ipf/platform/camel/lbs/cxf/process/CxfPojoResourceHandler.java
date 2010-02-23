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
package org.openehealth.ipf.platform.camel.lbs.cxf.process;

import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Message;
import org.apache.cxf.message.MessageContentsList;
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource;
import org.openehealth.ipf.commons.lbs.resource.ResourceFactory;
import org.openehealth.ipf.platform.camel.lbs.core.process.ResourceHandler;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.ws.Holder;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang.Validate.notNull;

/**
 * A handler for resources contained in a CXF Soap POJO message.
 * <p>
 * This handler should be used for CXF endpoints using the data format 
 * <code>POJO</code>. Can be used with SwA and MTOM.
 * @author Jens Riemschneider
 */
public class CxfPojoResourceHandler implements ResourceHandler {
    private static final String RESOURCE_ID_PARAM_PREFIX = "org.openehealth.ipf.platform.camel.lbs.cxf.CxfPojoResourceHandler.Param.";
    
    private final ResourceFactory resourceFactory;

    /**
     * Constructs the handler
     * @param resourceFactory
     *          the factory for resources that are extracted by this handler
     */    
    public CxfPojoResourceHandler(ResourceFactory resourceFactory) {
        notNull(resourceFactory, "resourceFactory cannot be null");
        this.resourceFactory = resourceFactory;
    }       

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.core.process.ResourceHandler#handle(org.apache.camel.Message)
     */
    @Override
    public Collection<ResourceDataSource> extract(String unitOfWorkId, Message message) throws Exception {
    	String subUnit = getSubUnit(unitOfWorkId);
        return extractFromParams(subUnit, getParams(message));
    }

    private String getSubUnit(String unitOfWorkId) {
		return unitOfWorkId + ".cxf";
	}

	private List<Object> getParams(Message message) {
        try {
            MessageContentsList params = message.getMandatoryBody(MessageContentsList.class);
            if (params == null) {
                return Collections.emptyList();
            }
            return params;            
        }
        catch (InvalidPayloadException e) {
            // This is ok. This message is not intended to be processed by this handler
        }
        return Collections.emptyList();
    }

    private Collection<ResourceDataSource> extractFromParams(String subUnit, List<Object> params) throws IOException {
        List<ResourceDataSource> resources = new ArrayList<ResourceDataSource>(); 
        for (int idx = 0; idx < params.size(); ++idx) {
            Object param = params.get(idx);
            if (param instanceof DataHandler) {
                DataHandler dataHandler = (DataHandler) param;
                ResourceDataSource dataSource = extractFromDataHandler(subUnit, dataHandler, idx);
                resources.add(dataSource);
                params.set(idx, new DataHandler(dataSource));
            }
            else if (param instanceof Holder) {
                @SuppressWarnings("unchecked") // Ok, because of instanceof check  
                Holder<DataHandler> holder = (Holder<DataHandler>) param;
                if (holder.value instanceof DataHandler) {
                    DataHandler dataHandler = holder.value;
                    ResourceDataSource dataSource = extractFromDataHandler(subUnit, dataHandler, idx);
                    resources.add(dataSource);
                    holder.value = new DataHandler(dataSource);
                }
            }
        }
        return resources;
    }

    private ResourceDataSource extractFromDataHandler(String subUnit, DataHandler handler, int paramIdx) throws IOException {
        InputStream inputStream = handler.getInputStream();
        try {
            String contentType = handler.getContentType();
            String id = RESOURCE_ID_PARAM_PREFIX + paramIdx;
            return resourceFactory.createResource(subUnit, contentType, null, id, inputStream);
        }
        finally {
            inputStream.close();
        }
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.camel.lbs.core.process.ResourceHandler#integrate(org.apache.camel.Message, java.util.Map)
     */
    @Override
    public void integrate(Message message) {
        // Does nothing because resource are already integrated
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

    @Override
    public Collection<? extends ResourceDataSource> getRequiredResources(Message message) {
        List<ResourceDataSource> resources = new ArrayList<ResourceDataSource>(); 
        List<Object> params = getParams(message);
        for (Object param : params) {
            ResourceDataSource resource = getResource(param);
            if (resource != null) {
                resources.add(resource);
            }
        }
        return resources;
    }
                                       
    private ResourceDataSource getResource(Object param) {
        if (param instanceof DataHandler) {
            return getResource((DataHandler) param);
        }
        else if (param instanceof Holder) {
            Holder holder = (Holder) param;
            if (holder.value instanceof DataHandler) {
                return getResource((DataHandler) holder.value);
            }
        }
        return null;
    }

    private ResourceDataSource getResource(DataHandler handler) {
        DataSource dataSource = handler.getDataSource();
        if (dataSource instanceof ResourceDataSource) {
            return (ResourceDataSource) dataSource;
        }
        return null;
    }
}
