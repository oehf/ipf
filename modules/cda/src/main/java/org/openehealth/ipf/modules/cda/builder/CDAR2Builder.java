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

import java.util.Collection;
import java.util.HashSet;

import groovy.lang.Closure;
import groovy.util.Factory;
import groovytools.builder.MetaBuilder;
import groovytools.builder.MetaObjectGraphBuilder;
import groovytools.builder.SchemaNode;

public class CDAR2Builder extends MetaBuilder {

    private BuilderDefinitionLoader loader;
    private Collection<String> loaded = new HashSet<String>();

    public CDAR2Builder() {
        super();
        setDefaultBuildNodeFactory(new CDAR2Factory());
        initDefinitionLoader();
        loader.load(loaded);
    }

    public CDAR2Builder(ClassLoader cl) {
        super(cl);
        setDefaultBuildNodeFactory(new CDAR2Factory());
        initDefinitionLoader();
        loader.load(loaded);
    }

    protected MetaObjectGraphBuilder createMetaObjectGraphBuilder(
            SchemaNode defaultSchema, Factory defaultNodeFactory,
            Closure objectVisitor) {
        return new DynamicCDAR2MetaObjectGraphBuilder(this, defaultSchema,
                defaultNodeFactory, objectVisitor);
    }

    protected void initDefinitionLoader() {
        setLoader(new CDAR2DefinitionLoader(this));
    }

    public BuilderDefinitionLoader getLoader() {
        return loader;
    }

    public void setLoader(BuilderDefinitionLoader loader) {
        this.loader = loader;
    }

}
