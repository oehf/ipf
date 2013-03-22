/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.osgi.karaf.commands;

import groovy.lang.GroovySystem;
import groovy.lang.MetaMethod;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.codehaus.groovy.runtime.m12n.ExtensionModule;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;

import java.util.List;

/**
 * @author Boris Stanojevic
 */
@Command(scope = "ipf", name = "listextensions", description="Lists all Groovy Extension Methods available")
public class ListExtensionMethodsCommand extends OsgiCommandSupport {


    @Override
    protected Object doExecute() throws Exception {
        List<ExtensionModule> modulesList = ((MetaClassRegistryImpl) GroovySystem
                .getMetaClassRegistry())
                .getModuleRegistry()
                .getModules();
        for (ExtensionModule module: modulesList){
            System.out.println("\nMODULE: " + module.getName());
            System.out.println("===================================== ");
            for (MetaMethod method: module.getMetaMethods()){
                System.out.println("\t-> " + method.getSignature());
            }
        }
        return null;
    }
}
