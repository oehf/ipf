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

import org.codehaus.groovy.runtime.InvokerHelper
import org.openhealthtools.ihe.common.cdar2.CDAR2Package

import groovytools.builder.PropertyException
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.impl.EClassImpl
import org.eclipse.emf.ecore.impl.EAttributeImpl
import org.eclipse.emf.common.util.EList

/**
 * @author Christian Ohr
 */
public class CDAR2Factory extends AbstractFactory{
	
	static def ePackage = org.openhealthtools.ihe.common.cdar2.CDAR2Package.eINSTANCE
	static def f = org.openhealthtools.ihe.common.cdar2.CDAR2Factory.eINSTANCE
	static def factories = [:]
	def type
	
	CDAR2Factory() {
		super()
	}
	
	protected CDAR2Factory(String s) {
		this()
		this.type = InvokerHelper.getProperty(CDAR2Package.Literals, s)
	}
	
	protected CDAR2Factory(EClassifier e) {
		this()
		this.type = e
	}
	
	Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attrs) {
		// Use of a specific library
		if (type == null) {
			return super.newInstance(builder, name, value, attrs)
		}
		def object
		// Create CDAR2 object directly from String, is possible
		if (type instanceof org.eclipse.emf.ecore.EDataType 
		&& value && value instanceof String) {
			try {
				object = f.createFromString(type, value)
			} catch (IllegalArgumentException e) {
				// Emit warning?
				object = f.create(type)
				object.setText(value)
			}
		} else {      
			// Create CDAR2 object and set value of attributes afterwards
			object = f.create(type)
			// TODO: review this code -> is not O.K. if onHandleNodeAttributes returns 'true'
			if (attrs) {
				attrs.each(){ key, val ->
               if (val instanceof String) {
                  def evaluatedValue
                  try {
					 evaluatedValue = createAttributeByNameFromString(object.class, key, val);
                  } catch (Exception e) {
                     evaluatedValue = val
                  }
				  InvokerHelper.setProperty(object, key, evaluatedValue)
               } else {
                  InvokerHelper.setProperty(object, key, val)
               }
				}
			}
			if (value) {
				if (value instanceof String) {
					object.setText(value)
				} else if (value instanceof Number) {
					object.setNumber(value)
				}        	 
			}
		}
		return object
	}
	
	int defaultTypeResolution(String name) {
		null
	}
	
	/*
	 * We need to take care ourselves that non-collection children are connected to
	 * the parent.
	 */
	public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
		if (child && parent) {
			def propertyName = builder.currentSchema.attribute('property')
			propertyName = propertyName ?: builder.currentName
			if (parent.metaClass.hasProperty(parent, propertyName))  {      
				InvokerHelper.setProperty(parent, propertyName, child)
			} else {
				throw new PropertyException("Property $propertyName: unknown for " 
				+ builder.parentContext?.get('_CURRENT_NAME_'))
			}
		}
		
	}
	
	/*
	 * @see AbstractFactory.onHandleNodeAttributes
	 */
	public boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
		return false
	}
	
	/**
	 * Reflect the attribute type and instanciate it properly from factory
	 * This needs a revision if no factory is found: for generic Boolean, Real etc. values
	 */
	protected def createAttributeByNameFromString(Class klass, String key, String value){
		EClassImpl classifierImpl = (EClassImpl) ePackage.getEClassifier(klass.getInterfaces()[0].getSimpleName());
		if (classifierImpl){
			EList eList = classifierImpl.getEAllAttributes();
			for ( Iterator i = eList.iterator(); i.hasNext(); ) {
				EAttributeImpl attrImpl = (EAttributeImpl)i.next();
				if (attrImpl.getName().equals(key)){
					def instance = f.createFromString(attrImpl.basicGetEAttributeType(), value);
					return instance
				}
			}    
		}
		return value
	}
	
	static def factoryFor(String name) {
		if (!factories.containsKey(name)) {
			synchronized(this) {
				factories.put(name, new CDAR2Factory(name))
			}
		}
		factories.get(name)
	}
	
	
}
