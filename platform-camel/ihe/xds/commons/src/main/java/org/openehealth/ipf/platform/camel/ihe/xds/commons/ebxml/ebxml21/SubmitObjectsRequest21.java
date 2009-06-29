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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAssociation;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExtrinsicObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RegistryPackage;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.ExtrinsicObject21;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.RegistryPackage21;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.AssociationType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExtrinsicObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LeafRegistryObjectListType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectRefType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryPackageType;

/**
 * Encapsulation of {@link SubmitObjectsRequest}
 * @author Jens Riemschneider
 */
public class SubmitObjectsRequest21 implements org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.SubmitObjectsRequest {
    private final static org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.ObjectFactory rsFactory = 
        new org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.ObjectFactory();

    private final static org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory rimFactory = 
        new org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory();
    
    private final SubmitObjectsRequest submitObjectsRequest;
    private final ObjectLibrary objectLibrary;

    private SubmitObjectsRequest21(SubmitObjectsRequest submitObjectsRequest, ObjectLibrary objectLibrary) {
        notNull(submitObjectsRequest, "submitObjectsRequest cannot be null");
        notNull(objectLibrary, "objectLibrary cannot be null");
        
        this.submitObjectsRequest = submitObjectsRequest;
        this.objectLibrary = objectLibrary;
    }

    static SubmitObjectsRequest21 create(ObjectLibrary objectLibrary) {
        SubmitObjectsRequest request = rsFactory.createSubmitObjectsRequest();
        LeafRegistryObjectListType list = request.getLeafRegistryObjectList();
        if (list == null) {
            list = rimFactory.createLeafRegistryObjectListType();
            request.setLeafRegistryObjectList(list);
        }
        return new SubmitObjectsRequest21(request, objectLibrary);
    }

    public static SubmitObjectsRequest21 create(SubmitObjectsRequest submitObjectsRequest) {
        ObjectLibrary objectLibrary = new ObjectLibrary();
        SubmitObjectsRequest21 submitObjectsRequest21 = new SubmitObjectsRequest21(submitObjectsRequest, objectLibrary);
        
        for (Object obj : submitObjectsRequest21.getContents()) {
            ExtrinsicObjectType extrinsic = cast(obj, ExtrinsicObjectType.class);
            if (extrinsic != null) {
                objectLibrary.put(extrinsic.getId(), extrinsic);
            }
            ObjectRefType objectRef = cast(obj, ObjectRefType.class);
            if (objectRef != null) {
                objectLibrary.put(objectRef.getId(), objectRef);
            }
            RegistryPackageType regPackage = cast(obj, RegistryPackageType.class);
            if (regPackage != null) {
                objectLibrary.put(regPackage.getId(), regPackage);
            }
        }
        
        return submitObjectsRequest21;
    }

    @Override
    public void addAssociation(EbXMLAssociation association) {
        if (association != null) {
            getContents().add(((EbXMLAssociation21)association).getInternal());
        }        
    }

    @Override
    public void addExtrinsicObject(ExtrinsicObject extrinsic) {
        if (extrinsic != null) {
            getContents().add(((ExtrinsicObject21)extrinsic).getInternal());
        }        
    }

    @Override
    public void addRegistryPackage(RegistryPackage regPackage) {
        if (regPackage != null) {
            getContents().add(((RegistryPackage21)regPackage).getInternal());            
        }        
    }

    @Override
    public void addClassification(Classification classification) {
        if (classification != null) {
            getContents().add(((Classification21)classification).getInternal());
        }
    }
    
    @Override
    public List<EbXMLAssociation> getAssociations() {
        List<EbXMLAssociation> results = new ArrayList<EbXMLAssociation>();
        for (Object identifiable : getContents()) {
            AssociationType1 association = cast(identifiable, AssociationType1.class);            
            if (association != null) {
                results.add(EbXMLAssociation21.create(association, objectLibrary));
            }
        }
        
        return results;
    }

    @Override
    public List<ExtrinsicObject> getExtrinsicObjects(String objectType) {
        notNull(objectType, "objectType cannot be null");
        
        List<ExtrinsicObject> results = new ArrayList<ExtrinsicObject>();
        for (Object identifiable : getContents()) {
            ExtrinsicObjectType extrinsic = cast(identifiable, ExtrinsicObjectType.class);            
            if (extrinsic != null && objectType.equals(extrinsic.getObjectType())) {
                results.add(ExtrinsicObject21.create(extrinsic, objectLibrary));
            }
        }
        
        return results;
    }

    @Override
    public List<ExtrinsicObject> getExtrinsicObjects() {
        List<ExtrinsicObject> results = new ArrayList<ExtrinsicObject>();
        for (Object identifiable : getContents()) {
            ExtrinsicObjectType extrinsic = cast(identifiable, ExtrinsicObjectType.class);            
            if (extrinsic != null) {
                results.add(ExtrinsicObject21.create(extrinsic, objectLibrary));
            }
        }
        
        return results;
    }

    @Override
    public List<RegistryPackage> getRegistryPackages(String classificationNode) {
        notNull(classificationNode, "classificationNode cannot be null");

        Set<String> acceptedIds = getAcceptedIds(classificationNode);
        
        List<RegistryPackage> results = new ArrayList<RegistryPackage>();
        for (Object identifiable : getContents()) {
            RegistryPackageType regPackage = cast(identifiable, RegistryPackageType.class);            
            if (matchesFilter(regPackage, acceptedIds, classificationNode)) {
                results.add(RegistryPackage21.create(regPackage, objectLibrary));
            }
        }
        
        return results;
    }

    @Override
    public List<RegistryPackage> getRegistryPackages() {
        List<RegistryPackage> results = new ArrayList<RegistryPackage>();
        for (Object identifiable : getContents()) {
            RegistryPackageType regPackage = cast(identifiable, RegistryPackageType.class);            
            if (regPackage != null) {
                results.add(RegistryPackage21.create(regPackage, objectLibrary));
            }
        }
        
        return results;
    }
    
    
    public SubmitObjectsRequest getInternal() {
        return submitObjectsRequest;
    }

    private boolean matchesFilter(RegistryPackageType regPackage, Set<String> acceptedIds, String classificationNode) {
        if (regPackage == null) {
            return false;
        }
        
        return acceptedIds.contains(regPackage.getId()) 
                || hasClassificationNode(regPackage, classificationNode);
    }

    private boolean hasClassificationNode(RegistryPackageType regPackage, String classificationNode) {
        String id = regPackage.getId();
        if (id == null) {
            return false;
        }
        
        for (ClassificationType classification : regPackage.getClassification()) {
            String classifiedObjId = objectLibrary.getByObj(classification.getClassifiedObject());
            if (classificationNode.equals(classification.getClassificationNode()) 
                    && id.equals(classifiedObjId)) {
                return true;
            }
        }
        return false;
    }

    private Set<String> getAcceptedIds(String acceptedNode) {
        Set<String> acceptedIds = new HashSet<String>(); 
        for (Object identifiable : getContents()) {
            ClassificationType classification = cast(identifiable, ClassificationType.class);
            if (classification != null) {
                String node = objectLibrary.getByObj(classification.getClassificationNode());
                if (acceptedNode.equals(node)) {
                    acceptedIds.add(objectLibrary.getByObj(classification.getClassifiedObject()));
                }
            }
        }
        return acceptedIds;
    }

    private List<Object> getContents() {
        LeafRegistryObjectListType list = submitObjectsRequest.getLeafRegistryObjectList();
        if (list == null) {
            return Collections.emptyList();
        }
        return list.getObjectRefOrAssociationOrAuditableEvent();
    }

    private static <T> T cast(Object identifiable, Class<T> type) {
        if (type.isInstance(identifiable)) {
            return type.cast(identifiable);
        }
        return null;
    }
}
