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
import org.openhealthtools.ihe.common.cdar2.ANY
import org.openhealthtools.ihe.common.cdar2.impl.ANYImpl
import org.openhealthtools.ihe.common.cdar2.impl.ANYNonNullImpl

import groovytools.builder.PropertyException

import org.openhealthtools.ihe.common.cdar2.CDAR2Package
import org.eclipse.emf.ecore.EClassifier

/**
 * @author Christian Ohr
 */
public class CDAR2Factory extends AbstractFactory{
	
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
		    return value
		}
		def object
		// Create CDAR2 object directly from String if possible
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
			// Create CDAR2 object
			object = f.create(type)
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
	
	/**
	 * Delegates the object instanciation to the underlieing factory
	 * @param type
	 * @papram value
	 * @return type instance object 
	 */
	public def createFromString(Object type, String value){
		return f.createFromString(type, value) 
	}
	
	/**
	 * @param name
	 * @return factory instance for name
	 */
	static def factoryFor(String name) {
		if (!factories.containsKey(name)) {
			synchronized(this) {
				factories.put(name, new CDAR2Factory(name))
			}
		}
		factories.get(name)
	}
	
}
