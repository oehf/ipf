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

import groovytools.builder.MetaBuilder;
import groovytools.builder.MetaBuilderException;

import java.io.IOException;
import java.util.Collection;

public class CDAR2DefinitionLoader extends DefaultBuilderDefinitionLoader {

    public CDAR2DefinitionLoader(MetaBuilder builder) {
        super(builder);
    }

    @Override
    public void load(Collection<String> loaded) {
        try {
            doLoad("/builders/AnyBuilder.groovy", loaded);
            doLoad("/builders/DataTypeBuilder.groovy", loaded);
            doLoad("/builders/InfrastructureBuilder.groovy", loaded);
            doLoad("/builders/EntityBuilder.groovy", loaded);
            doLoad("/builders/RoleBuilder.groovy", loaded);
            doLoad("/builders/ParticipationBuilder.groovy", loaded);
            doLoad("/builders/ActBuilder.groovy", loaded);
            doLoad("/builders/NarrativesBuilder.groovy", loaded);
            doLoad("/builders/ActRelationshipBuilder.groovy", loaded);
            doLoad("/supportbuilders/CodeBuilder.groovy", loaded);
        } catch (IOException e) {
            throw new MetaBuilderException(e);
        }

    }


}
