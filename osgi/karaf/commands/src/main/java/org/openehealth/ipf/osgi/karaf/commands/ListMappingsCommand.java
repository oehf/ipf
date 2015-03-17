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

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.openehealth.ipf.commons.map.BidiMappingService;
import org.openehealth.ipf.commons.map.MappingService;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Boris Stanojevic
 */
@Command(scope = "ipf", name = "listmappings", description="Lists all Mappings available on IPF Mapping Service")
public class ListMappingsCommand extends OsgiCommandSupport {


    @Override
    protected Object doExecute() throws Exception {
        BidiMappingService mappingService = getService(BidiMappingService.class,
                getBundleContext().getServiceReference(MappingService.class.getCanonicalName()));
        if (mappingService != null && mappingService.getMap() != null){
            System.out.println("\nAvailable Mappings(" + mappingService.getMap().size() + ")");
            System.out.println("======================== ");
            for (Object key : mappingService.getMap().keySet()) {
                System.out.println("\n\tKey: " + key);
                Map map = mappingService.getMap().get(key);
                System.out.println("\t-------------------------------------------------");
                for (Object o : map.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    System.out.println("\t\t" + entry.getKey() + " -> " + entry.getValue());
                }
            }
        }
        return null;
    }
}
