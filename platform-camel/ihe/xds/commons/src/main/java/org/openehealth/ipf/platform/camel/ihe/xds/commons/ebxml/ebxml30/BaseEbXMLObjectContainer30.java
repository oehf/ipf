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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAssociation;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLObjectContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExtrinsicObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RegistryPackage;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.AssociationType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ExtrinsicObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.IdentifiableType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.RegistryPackageType;

/**
 * Base class for requests and responses that contain various ebXML 3.0
 * objects.
 * @author Jens Riemschneider
 */
public abstract class BaseEbXMLObjectContainer30 implements EbXMLObjectContainer {
    private final static org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory rimFactory = 
        new org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory();
    
    private final ObjectLibrary objectLibrary;

    public ObjectLibrary getObjectLibrary() {
        return objectLibrary;
    }
    
    /**
     * Constructs the container.
     * @param objectLibrary
     *          the object library to use.
     */
    BaseEbXMLObjectContainer30(ObjectLibrary objectLibrary) {
        notNull(objectLibrary, "objLibrary cannot be null");
        this.objectLibrary = objectLibrary;
    }
    
    @Override
    public void addAssociation(EbXMLAssociation association) {
        if (association != null) {
            AssociationType1 internal = ((EbXMLAssociation30)association).getInternal();
            getContents().add(rimFactory.createAssociation(internal));
        }        
    }

    @Override
    public void addExtrinsicObject(ExtrinsicObject extrinsic) {
        if (extrinsic != null) {
            ExtrinsicObjectType internal = ((ExtrinsicObject30)extrinsic).getInternal();
            getContents().add(rimFactory.createExtrinsicObject(internal));
        }        
    }

    @Override
    public void addRegistryPackage(RegistryPackage regPackage) {
        if (regPackage != null) {
            RegistryPackageType internal = ((RegistryPackage30)regPackage).getInternal();
            getContents().add(rimFactory.createRegistryPackage(internal));
        }        
    }

    @Override
    public List<EbXMLAssociation> getAssociations() {
        List<EbXMLAssociation> results = new ArrayList<EbXMLAssociation>();
        for (JAXBElement<? extends IdentifiableType> identifiable : getContents()) {
            AssociationType1 association = cast(identifiable, AssociationType1.class);            
            if (association != null) {
                results.add(EbXMLAssociation30.create(association));
            }
        }
        
        return results;
    }

    @Override
    public List<ExtrinsicObject> getExtrinsicObjects(String objectType) {
        notNull(objectType, "objectType cannot be null");
        
        List<ExtrinsicObject> results = new ArrayList<ExtrinsicObject>();
        for (JAXBElement<? extends IdentifiableType> identifiable : getContents()) {
            ExtrinsicObjectType extrinsic = cast(identifiable, ExtrinsicObjectType.class);            
            if (extrinsic != null && objectType.equals(extrinsic.getObjectType())) {
                results.add(ExtrinsicObject30.create(extrinsic));
            }
        }
        
        return results;
    }

    @Override
    public List<ExtrinsicObject> getExtrinsicObjects() {
        List<ExtrinsicObject> results = new ArrayList<ExtrinsicObject>();
        for (JAXBElement<? extends IdentifiableType> identifiable : getContents()) {
            ExtrinsicObjectType extrinsic = cast(identifiable, ExtrinsicObjectType.class);            
            if (extrinsic != null) {
                results.add(ExtrinsicObject30.create(extrinsic));
            }
        }
        
        return results;
    }

    @Override
    public List<RegistryPackage> getRegistryPackages(String classificationNode) {
        notNull(classificationNode, "classificationNode cannot be null");
    
        Set<String> acceptedIds = getAcceptedIds(classificationNode);
        
        List<RegistryPackage> results = new ArrayList<RegistryPackage>();
        for (JAXBElement<? extends IdentifiableType> identifiable : getContents()) {
            RegistryPackageType regPackage = cast(identifiable, RegistryPackageType.class);            
            if (matchesFilter(regPackage, acceptedIds, classificationNode)) {
                results.add(RegistryPackage30.create(regPackage));
            }
        }
        
        return results;
    }

    @Override
    public List<RegistryPackage> getRegistryPackages() {
        List<RegistryPackage> results = new ArrayList<RegistryPackage>();
        for (JAXBElement<? extends IdentifiableType> identifiable : getContents()) {
            RegistryPackageType regPackage = cast(identifiable, RegistryPackageType.class);            
            if (regPackage != null) {
                results.add(RegistryPackage30.create(regPackage));
            }
        }
        
        return results;
    }

    @Override
    public void addClassification(Classification classification) {
        if (classification != null) {
            ClassificationType internal = ((Classification30)classification).getInternal();
            getContents().add(rimFactory.createClassification(internal));
        }
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
            if (classificationNode.equals(classification.getClassificationNode()) 
                    && id.equals(classification.getClassifiedObject())) {
                return true;
            }
        }
        return false;
    }

    private Set<String> getAcceptedIds(String classificationNode) {
        Set<String> acceptedIds = new HashSet<String>(); 
        for (JAXBElement<? extends IdentifiableType> identifiable : getContents()) {
            ClassificationType classification = cast(identifiable, ClassificationType.class);
            if (classification != null && classificationNode.equals(classification.getClassificationNode())) {
                acceptedIds.add(classification.getClassifiedObject());
            }
        }
        return acceptedIds;
    }

    private <T extends IdentifiableType> T cast(JAXBElement<? extends IdentifiableType> identifiable, Class<T> type) {
        if (identifiable.getDeclaredType() == type) {
            return type.cast(identifiable.getValue());
        }
        return null;
    }
    
    /**
     * @return retrieves the list of contained objects.
     */
    abstract List<JAXBElement<? extends IdentifiableType>> getContents();
}