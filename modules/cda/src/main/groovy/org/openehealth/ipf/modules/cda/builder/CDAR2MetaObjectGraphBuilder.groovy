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
package org.openehealth.ipf.modules.cda.builder

import groovytools.builder.*
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.common.util.Enumerator
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.impl.EClassImpl
import org.eclipse.emf.ecore.impl.EAttributeImpl
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * Custom ObjectGraphBuilder. Resolves factories attributes as String
 * as key to a CDAR2Factory, not as class name like the base class
 *
 * @author Christian Ohr
 * @author Stefan Ivanov
 */
public class CDAR2MetaObjectGraphBuilder extends MetaObjectGraphBuilder {

  static def ePackage = org.openhealthtools.ihe.common.cdar2.CDAR2Package.eINSTANCE
  private static final Log LOG = LogFactory.getLog(CDAR2MetaObjectGraphBuilder.class)

  public CDAR2MetaObjectGraphBuilder(MetaBuilder metaBuilder, SchemaNode defaultSchema, Factory defaultFactory) {
    super(metaBuilder, defaultSchema, defaultFactory, null)
  }

  public CDAR2MetaObjectGraphBuilder(MetaBuilder metaBuilder, SchemaNode defaultSchema, Factory defaultFactory, Closure objectVisitor) {
    super(metaBuilder, defaultSchema, defaultFactory, objectVisitor)
  }

  protected Factory resolveFactory(Object name, Map attrs, Object value) {
    SchemaNode schema = getCurrentSchema();
    Object factoryAttr = findSchemaAttribute(schema, "factory")
    if (factoryAttr instanceof String) {
      // Replace String attribute by Factory instance
      def factory = CDAR2Factory.factoryFor(factoryAttr)
      schema.attributes().put("factory", factory)
    }
    super.resolveFactory(name, attrs, value)
  }
  

  /**
   * For EObject instances evaluate the value using the model factory
   * In cases the MetaBuilder's property attribute is used, evaluate also
   * the objects attribute name
   *
   * @see MetaObjectGraphBuilder#setVariable(Object, SchemaNode, String, Object)
   */
  public void setVariable(Object node, SchemaNode schema, String name, Object value) {
    if (node instanceof EObject) {
      SchemaNode mergedProperties = getMergedProperties(schema);
      SchemaNode propertySchema = (SchemaNode) mergedProperties.firstChild(name);
      if (propertySchema) {
        Object propertyAttr = propertySchema.attribute("property");
        if (propertyAttr) {
          String propertyName = resolvePropertyAttrName(name, propertySchema, propertyAttr)
          value = evaluateAttrValue(node, propertyName, value, propertySchema)
        } else {
          value = evaluateAttrValue(node, name, value, propertySchema)
        }
      }
			super.setVariable(node, schema, name, value)
    } else {
      super.setVariable(node, schema, name, value)
    }
  }

  /**
   * Reads the 'property' attribute from the meta builder definition
   * Allows using it for value instaciation.
   */
  private def evaluateAttrValue(Object node, String name, Object val, SchemaNode propertySchema) {
    String propertyName = name
    if (val instanceof String) {
      try {
        return createAttributeByNameFromString(node.class, propertyName, val);
      } catch (Exception e) {
        LOG.warn("Cann't evaluate the value from String for the property " + propertyName + " : " + e.getMessage());
      }
    } else {
      return val
    }
  }

  /**
   * In the cases when the propery attribute name is overriden
   * using the Meta Builder's 'property' attribute
   * the name is resolved and used to reflect in the EMF classifier
   * definition
   */
  private String resolvePropertyAttrName(String propertyName, SchemaNode propertySchema, Object propertyAttr) {
    if (propertyAttr != null) {
      if (propertyAttr instanceof String) {
        return (String) propertyAttr
      }
      else {
        throw this.getMetaBuilder().createPropertyException(propertyName, "'property' attribute of schema does not specify a string or closure.");
      }
    }
    else {
      return (String) propertySchema.name()
    }
  }

  /**
   * Reflect the attribute type and instanciate it properly from factory
   * This needs a revision if no factory is found: for generic Boolean, Real etc. values
   */
  protected def createAttributeByNameFromString(Class klass, String key, String value) {
    EClassImpl classifierImpl = (EClassImpl) ePackage.getEClassifier(klass.getInterfaces()[0].getSimpleName());
    if (classifierImpl) {
      EList eList = classifierImpl.getEAllAttributes();
      for (Iterator i = eList.iterator(); i.hasNext();) {
        EAttributeImpl attrImpl = (EAttributeImpl) i.next();
        if (attrImpl.getName().equals(key)) {
          Class valueClass = attrImpl.getEAttributeType().getInstanceClass()
          if (Enumerator.class.isAssignableFrom(valueClass)) { //create enumeration instance
            def instance = this.getMetaBuilder().getDefaultBuildNodeFactory().createFromString(attrImpl.basicGetEAttributeType(), value);
            return instance
          } else if (boolean.class.isAssignableFrom(valueClass) ||
              Boolean.class.isAssignableFrom(valueClass)) {
            return Boolean.newInstance(value)
          } else if (Number.class.isAssignableFrom(valueClass)) { //call the string constructor
            return valueClass.newInstance(value)
          }
        }
      }
      return value
    }
  }
  

}
