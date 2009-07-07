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
import java.lang.ClassLoader


/**
 * CDAR2Builder uses the CDAR2Factory as default factory
 * 
 * @author Christian Ohr
 */
public class CDAR2Builder extends MetaBuilder{
	
	public CDAR2Builder(ClassLoader loader) {
	    super(loader)
		setDefaultBuildNodeFactory(new CDAR2Factory())
		initializeBuilders()
        new CDAR2ModelExtension().extensions.call()
	}
		
	protected MetaObjectGraphBuilder createMetaObjectGraphBuilder(SchemaNode defaultSchema, Factory defaultNodeFactory, Closure objectVisitor) {
		return new CDAR2MetaObjectGraphBuilder(this, defaultSchema, defaultNodeFactory, objectVisitor);
	} 

	
	protected void initializeBuilders() {
		anyBuilder()
		dataTypeBuilder()
		infrastructureRootBuilder()
		entityBuilder()
		roleBuilder()
		participationBuilder()
        narrativesBuilder()
		actBuilder()
		actRelationshipBuilder()
		codeBuilder()
	}
	
	
	protected void anyBuilder() {
		define {
			_any(factory:'ANY') {
				properties {
                  nullFlavor(factory:'NULL_FLAVOR')
                }
			}
		}
	}
	
	protected void dataTypeBuilder() {
		define(getClass().getResource('/builders/DataTypeBuilder.groovy'))
	}
		
	protected void infrastructureRootBuilder() {
		define(getClass().getResource('/builders/InfrastructureBuilder.groovy'))
	}
	
	protected void entityBuilder() {
		define(getClass().getResource('/builders/EntityBuilder.groovy'))
	}
	
	protected void roleBuilder() {
		define(getClass().getResource('/builders/RoleBuilder.groovy'))
	}
		
	protected void participationBuilder() {
		define(getClass().getResource('/builders/ParticipationBuilder.groovy'))
	}

	protected void actBuilder() {
		define(getClass().getResource('/builders/ActBuilder.groovy'))
	}

	protected void narrativesBuilder() {
		define(getClass().getResource('/builders/NarrativesBuilder.groovy'))
	}

	protected void actRelationshipBuilder() {
		define(getClass().getResource('/builders/ActRelationshipBuilder.groovy'))
	}
	
	protected void codeBuilder() {
		define(getClass().getResource('/supportbuilders/CodeBuilder.groovy'))
	}
	
	
}
