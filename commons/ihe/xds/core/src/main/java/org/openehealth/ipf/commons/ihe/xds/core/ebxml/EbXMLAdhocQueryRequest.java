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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml;

/**
 * Encapsulation of the ebXML classes for {@code AdhocQueryRequest}. 
 * <p>
 * This class contains convenience methods and provides a version independent
 * abstraction of the ebXML data structure.
 * @author Jens Riemschneider
 */
public interface EbXMLAdhocQueryRequest extends EbXMLSlotList {
    /**
     * @param sql
     *          SQL string used by the query.
     */
    void setSql(String sql);
    
    /**
     * @return SQL string used by the query.
     */
    String getSql();
    
    /**
     * @param returnType
     *          the type of objects that the query should return.
     */
    void setReturnType(String returnType);

    /**
     * @return the type of objects that the query should return.
     */
    String getReturnType();
    
    /**
     * @param id  
     *          the id of the query.
     */
    void setId(String id);
    
    /**
     * @return the id of the query.
     */
    String getId();
    
    /**
     * @param homeCommunityID
     *          home community ID.
     */
    void setHome(String homeCommunityID);
    
    /**
     * @return home community ID.
     */
    String getHome();

    /**
     * @return the ebXML object wrapped by this object.
     */
    Object getInternal();
}
