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
package org.openehealth.ipf.modules.hl7.parser

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.parser.*

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * Custom ModelClassFactory that allows to overrule or augment the default HL7 model
 * classes. This is necessary to support variants/dialects of HL7. Model classes
 * to be used can be explicitly assigned to the CustomModelClassFactory. In the
 * original DefaultModelClassFactory of HAPI, only one fix set of model classes
 * could be used.
 * <p>
 * customModelClasses entries map HL7 versions to a list of package names. Each
 * package is looked up for the desired model class by appending "message",
 * "segment", "datatype" or "group" to the package name, respectively. If no
 * custom class could be found, it delegates to the default HAPI implementation
 * (DefaultModelClassFactory).
 * 
 * @author Christian Ohr
 * @author Marek Václavík
 * 
 */
public class CustomModelClassFactory implements ModelClassFactory{
	
	ModelClassFactory defaultFactory
	Map<String, String[]> customModelClasses
	
	private static Log LOG = LogFactory.getLog(CustomModelClassFactory.class)
	
	CustomModelClassFactory() {
		defaultFactory = new DefaultModelClassFactory()
	}
	
	CustomModelClassFactory(Map<String, String[]> map) {
		defaultFactory = new DefaultModelClassFactory()
		customModelClasses = map
	}
	
	/* (non-Javadoc)
	 * @see ca.uhn.hl7v2.parser.ModelClassFactory#getMessageClass(java.lang.String, java.lang.String, boolean)
	 */
	public Class getMessageClass(String name, String version, boolean isExplicit){
		if (!isExplicit)
			name = Parser.getMessageStructureForEvent(name, version);
		findClass("message", name, version) ?: defaultFactory.getMessageClass(name, version, isExplicit)	
	}
	
	/* (non-Javadoc)
	 * @see ca.uhn.hl7v2.parser.ModelClassFactory#getGroupClass(java.lang.String, java.lang.String)
	 */
	public Class getGroupClass(String name, String version){
		findClass("group", name, version) ?: defaultFactory.getGroupClass(name, version)
	}
	
	/* (non-Javadoc)
	 * @see ca.uhn.hl7v2.parser.ModelClassFactory#getSegmentClass(java.lang.String, java.lang.String)
	 */
	public Class getSegmentClass(String name, String version){
		findClass("segment", name, version) ?: defaultFactory.getSegmentClass(name, version)
	}
	
	/* (non-Javadoc)
	 * @see ca.uhn.hl7v2.parser.ModelClassFactory#getTypeClass(java.lang.String, java.lang.String)
	 */
	public Class getTypeClass(String name, String version){
		findClass("datatype", name, version) ?: defaultFactory.getTypeClass(name, version)
	}
	
	// Finds appropriate classes to be loaded for the given structure/type
	protected Class findClass(String subpackage, String name, String version) {
		if (!Parser.validVersion(version)) {
			throw new HL7Exception("HL7 version $version is not supported",
			HL7Exception.UNSUPPORTED_VERSION_ID);
		}
		def classLoaded
		customModelClasses?.getAt(version)?.find {
			try {
				def sep = it.endsWith('.') ? '' : '.'
				def fullyQualifiedName = "${it}${sep}${subpackage}.${name}"        		
				classLoaded = Class.forName(fullyQualifiedName)
				LOG.debug("Found ${fullyQualifiedName} in custom HL7 model definitions")
			} catch (Exception e) {
				// do nothing
			}
			return classLoaded
		}
		return classLoaded 
	}
	
}
