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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21;

import static org.apache.commons.lang3.Validate.noNullElements;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAssociation;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectContainer;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.AssociationType1;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ClassificationType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ExtrinsicObjectType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ObjectRefType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.RegistryPackageType;

/**
 * Base class for requests and responses that contain various ebXML 2.1
 * objects.
 * @author Jens Riemschneider
 */
abstract class EbXMLObjectContainer21 implements EbXMLObjectContainer {
    private final EbXMLObjectLibrary objectLibrary;

    @Override
    public EbXMLObjectLibrary getObjectLibrary() {
        return objectLibrary;
    }

    /**
     * Fills the object Library based on the contents.
     */
    protected void fillObjectLibrary() {
        for (Object obj : getContents()) {
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
            AssociationType1 association = cast(obj, AssociationType1.class);
            if (association != null) {
                objectLibrary.put(association.getId(), association);
            }
            ClassificationType classification = cast(obj, ClassificationType.class);
            if (classification != null) {
                if (classification.getClassificationNode() instanceof ObjectRefType) {
                    ObjectRefType node = (ObjectRefType) classification.getClassificationNode();
                    objectLibrary.put(node.getId(), node);
                }
            }
        }
    }

    /**
     * Casts an object from the contents into the given type.
     * @param <T>
     *          the type to cast to.
     * @param obj
     *          the object to cast.
     * @param type
     *          the type to cast to.
     * @return the result of the cast or <code>null</code> if the object wasn't of the given type.
     */
    protected static <T> T cast(Object obj, Class<T> type) {
        if (type.isInstance(obj)) {
            return type.cast(obj);
        }
        return null;
    }

    /**
     * Constructs the container.
     * @param objectLibrary
     *          the object library to use.
     */
    protected EbXMLObjectContainer21(EbXMLObjectLibrary objectLibrary) {
        notNull(objectLibrary, "objLibrary cannot be null");
        this.objectLibrary = objectLibrary;
    }

    @Override
    public void addAssociation(EbXMLAssociation association) {
        if (association != null) {
            getContents().add(((EbXMLAssociation21)association).getInternal());
        }        
    }

    @Override
    public void addExtrinsicObject(EbXMLExtrinsicObject extrinsic) {
        if (extrinsic != null) {
            getContents().add(extrinsic.getInternal());
        }        
    }

    @Override
    public void addRegistryPackage(EbXMLRegistryPackage regPackage) {
        if (regPackage != null) {
            getContents().add(regPackage.getInternal());            
        }        
    }

    @Override
    public void addClassification(EbXMLClassification classification) {
        if (classification != null) {
            getContents().add(((EbXMLClassification21)classification).getInternal());
        }
    }
    
    @Override
    public List<EbXMLAssociation> getAssociations() {
        List<EbXMLAssociation> results = new ArrayList<EbXMLAssociation>();
        for (Object identifiable : getContents()) {
            AssociationType1 association = cast(identifiable, AssociationType1.class);            
            if (association != null) {
                results.add(new EbXMLAssociation21(association, objectLibrary));
            }
        }
        
        return results;
    }

    @Override
    public List<EbXMLExtrinsicObject> getExtrinsicObjects(String... objectTypes) {
        noNullElements(objectTypes, "objectTypes cannot be null or contain null elements");
        
        List<EbXMLExtrinsicObject> results = new ArrayList<EbXMLExtrinsicObject>();
        for (Object identifiable : getContents()) {
            ExtrinsicObjectType extrinsic = cast(identifiable, ExtrinsicObjectType.class);            
            if (extrinsic != null) {
                for (String objectType : objectTypes) {
                    if (objectType.equals(extrinsic.getObjectType())) {
                        results.add(new EbXMLExtrinsicObject21(extrinsic, objectLibrary));
                        break;
                    }
                }
            }
        }
        
        return results;
    }

    @Override
    public List<EbXMLExtrinsicObject> getExtrinsicObjects() {
        List<EbXMLExtrinsicObject> results = new ArrayList<EbXMLExtrinsicObject>();
        for (Object identifiable : getContents()) {
            ExtrinsicObjectType extrinsic = cast(identifiable, ExtrinsicObjectType.class);            
            if (extrinsic != null) {
                results.add(new EbXMLExtrinsicObject21(extrinsic, objectLibrary));
            }
        }
        
        return results;
    }

    @Override
    public List<EbXMLRegistryPackage> getRegistryPackages(String classificationNode) {
        notNull(classificationNode, "classificationNode cannot be null");
    
        Set<String> acceptedIds = getAcceptedIds(classificationNode);
        
        List<EbXMLRegistryPackage> results = new ArrayList<EbXMLRegistryPackage>();
        for (Object identifiable : getContents()) {
            RegistryPackageType regPackage = cast(identifiable, RegistryPackageType.class);            
            if (matchesFilter(regPackage, acceptedIds, classificationNode)) {
                results.add(new EbXMLRegistryPackage21(regPackage, objectLibrary));
            }
        }
        
        return results;
    }

    @Override
    public List<EbXMLRegistryPackage> getRegistryPackages() {
        List<EbXMLRegistryPackage> results = new ArrayList<EbXMLRegistryPackage>();
        for (Object identifiable : getContents()) {
            RegistryPackageType regPackage = cast(identifiable, RegistryPackageType.class);            
            if (regPackage != null) {
                results.add(new EbXMLRegistryPackage21(regPackage, objectLibrary));
            }
        }
        
        return results;
    }

    @Override
    public List<EbXMLClassification> getClassifications() {
        List<EbXMLClassification> results = new ArrayList<EbXMLClassification>();
        for (Object identifiable : getContents()) {
            ClassificationType classification = cast(identifiable, ClassificationType.class);            
            if (classification != null) {
                results.add(new EbXMLClassification21(classification, objectLibrary));
            }
        }
        
        return results;
    }
    
    /**
     * @return retrieves the list of contained objects.
     */
    protected abstract List<Object> getContents();
    
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
}