/**
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
package org.openehealth.ipf.modules.cda.builder;

import groovy.lang.Closure;
import groovy.util.Factory;
import groovytools.builder.MetaBuilder;
import groovytools.builder.MetaObjectGraphBuilder;
import groovytools.builder.SchemaNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.openhealthtools.ihe.common.cdar2.CDAR2Package;

import java.util.Map;

public class CDAR2MetaObjectGraphBuilder extends MetaObjectGraphBuilder {

    static final CDAR2Package ePackage = CDAR2Package.eINSTANCE;
    private static final Logger LOG = LoggerFactory.getLogger(CDAR2MetaObjectGraphBuilder.class);
    
    public CDAR2MetaObjectGraphBuilder(MetaBuilder metaBuilder,
            SchemaNode defaultSchema, Factory defaultFactory) {
        super(metaBuilder, defaultSchema, defaultFactory, null);
    }

    public CDAR2MetaObjectGraphBuilder(MetaBuilder metaBuilder,
            SchemaNode defaultSchema, Factory defaultFactory,
            Closure objectVisitor) {
        super(metaBuilder, defaultSchema, defaultFactory, objectVisitor);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Factory resolveFactory(Object name, Map attrs, Object value) {
        SchemaNode schema = getCurrentSchema();
        Object factoryAttr = findSchemaAttribute(schema, "factory");

        if (factoryAttr == null) {
            // resolve property factory for inherited property definitions
            if (schema.parent().name() == "properties") {
                SchemaNode mergedProperties = getMergedProperties((SchemaNode) schema
                        .parent().parent());
                SchemaNode mergedNamedDefinition = (SchemaNode) mergedProperties
                        .firstChild(name.toString());
                factoryAttr = findSchemaAttribute(mergedNamedDefinition,
                        "factory");
            }
        }

        if (factoryAttr instanceof String) {
            // Replace String attribute by Factory instance

            Object factory = CDAR2Factory.factoryFor(factoryAttr.toString());
            schema.attributes().put("factory", factory);
        }
        // as of check attribute is not propagated to the extension deifinitions
        Object checkAttr = findSchemaAttribute(schema, "check");
        if (checkAttr != null)
            schema.attributes().put("check", checkAttr);
        return super.resolveFactory(name, attrs, value);
    }

    /**
     * For EObject instances evaluate the value using the model factory In cases
     * the MetaBuilder's property attribute is used, evaluate also the objects
     * attribute name
     * 
     * @see MetaObjectGraphBuilder#setVariable(Object, SchemaNode, String,
     *      Object)
     */
    @Override
    public void setVariable(Object node, SchemaNode schema, String name,
            Object value) {
        if (node instanceof EObject) {
            SchemaNode mergedProperties = getMergedProperties(schema);
            SchemaNode propertySchema = (SchemaNode) mergedProperties
                    .firstChild(name);
            if (propertySchema != null) {
                Object propertyAttr = propertySchema.attribute("property");
                if (propertyAttr != null) {
                    String propertyName = resolvePropertyAttrName(name,
                            propertySchema, propertyAttr);
                    value = evaluateAttrValue(node, propertyName, value);
                } else {
                    value = evaluateAttrValue(node, name, value);
                }
            }
            super.setVariable(node, schema, name, value);
        } else {
            super.setVariable(node, schema, name, value);
        }
    }

    /**
     * Reads the 'property' attribute from the meta builder definition Allows
     * using it for value instaciation.
     */
    private Object evaluateAttrValue(Object node, String name, Object val) {
        if (val instanceof String) {
            try {
                return createAttributeByNameFromString(node.getClass(),
                        name, val.toString());
            } catch (Exception e) {
                LOG.warn("Can't evaluate the value from String for the property {} : {}",
                                name, e.getMessage());
                return val;
            }
        } else {
            return val;
        }
    }

    /**
     * In the cases when the propery attribute name is overriden using the Meta
     * Builder's 'property' attribute the name is resolved and used to reflect
     * in the EMF classifier definition
     */
    private String resolvePropertyAttrName(String propertyName,
            SchemaNode propertySchema, Object propertyAttr) {
        if (propertyAttr != null) {
            if (propertyAttr instanceof String) {
                return (String) propertyAttr;
            } else {
                throw MetaBuilder
                        .createPropertyException(propertyName,
                                "'property' attribute of schema does not specify a string or closure.");
            }
        } else {
            return (String) propertySchema.name();
        }
    }

    /**
     * Reflect the attribute type and instanciate it properly from factory This
     * needs a revision if no factory is found: for generic Boolean, Real etc.
     * values
     */
    @SuppressWarnings("unchecked")
    protected Object createAttributeByNameFromString(Class klass, String key,
            String value) {
        EClassImpl classifierImpl = (EClassImpl) ePackage.getEClassifier(klass
                .getInterfaces()[0].getSimpleName());
        if (classifierImpl != null) {
            EList eList = classifierImpl.getEAllAttributes();
            for (Object anEList : eList) {
                EAttributeImpl attrImpl = (EAttributeImpl) anEList;
                if (attrImpl.getName().equals(key)) {
                    Class valueClass = attrImpl.getEAttributeType()
                            .getInstanceClass();
                    if (Enumerator.class.isAssignableFrom(valueClass)) { // create
                        // enumeration
                        // instance
                        Object instance = ((CDAR2Factory) getMetaBuilder()
                                .getDefaultBuildNodeFactory())
                                .createFromString(attrImpl
                                        .basicGetEAttributeType(), value);
                        return instance;
                    } else if (boolean.class.isAssignableFrom(valueClass)
                            || Boolean.class.isAssignableFrom(valueClass)) {
                        return value;
                    } else if (Number.class.isAssignableFrom(valueClass)) { // call
                        // the
                        // string
                        // constructor
                        return value;
                    }
                }
            }
            return value;
        } else {
            return null;
        }
    }
}
