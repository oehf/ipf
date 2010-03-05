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
package org.openehealth.ipf.commons.xml

import groovy.xml.MarkupBuilder
import groovy.util.XmlSlurper
import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.Node

/*
 * Routines for yielding of XML contents from a GPath Slurper to a Markup Builder.
 * @author Dmytro Rud
 */
class XmlYielder {
    
    /**
     * Yields the XML element represented by the given GPath result instance 
     * into the given target XML builder, using the given namespace URI  
     * as a default one, i.e. without prefix for it.
     */
    static void yieldElement(GPathResult source, MarkupBuilder target, String defaultNamespaceUri) {
        if (!source) {
            return
        }
        yieldElement(source.nodeIterator().next(), target, [(defaultNamespaceUri) : '*'])
    }
     
     
    /**
     * Yields the given source XML element into the given target XML builder,
     * using the provided set of pre-defined namespace prefixes.
     */
    static void yieldElement(Node source, MarkupBuilder target, Map<String, String> predefinedNamespaces) {
        if (!source) {
            return
        }
        
        Map<String, String> attributes = [:]
        Map<String, String> knownNamespaces = [:]
        knownNamespaces.putAll(predefinedNamespaces)

        String elementNsPrefix = getNsPrefix(source.namespaceURI(), knownNamespaces, attributes)

        for(attribute in source.attributes()) {
            String nsUri = source.attributeNamespaces[attribute.key]
            String nsPrefix
            if (elementNsPrefix && (knownNamespaces[nsUri] == '*')) {
                // attribute with the default namespace inside of an element with a custom namespace 
                nsPrefix = createNsPrefix(nsUri, knownNamespaces, attributes)
            } else {
                nsPrefix = getNsPrefix(nsUri, knownNamespaces, attributes)
            }
            
            attributes[nsPrefix + attribute.key] = attribute.value
        }

        target."${elementNsPrefix + source.name()}"(attributes) { 
            yieldChildren(source, target, knownNamespaces)
        }
    }
    

    /**
     * Yields child elements of the given source XML element into the given target 
     * XML builder, using the provided set of pre-defined namespace prefixes.
     */
    static void yieldChildren(Node source, MarkupBuilder target, Map<String, String> predefinedNamespaces) {
        if (!source) {
            return
        }
        for (child in source.children()) {
            if (child instanceof Node) {
                yieldElement(child, target, predefinedNamespaces)
            } else {
                target.yield(child, true)
            }
        }
    }

     
    /**
     * Yields child elements of the element represented by the given GPath result  
     * instance into the given target XML builder, using the given namespace URI  
     * as a default one, i.e. without prefix for it.
     */
    static void yieldChildren(GPathResult source, MarkupBuilder target, String defaultNamespaceUri) {
        if (!source) {
            return
        }
        yieldChildren(source.nodeIterator().next(), target, [(defaultNamespaceUri) : '*'])
    }

     
    /**
     * Returns namespace prefix for the given namespace URI.
     * When this URI is not known yet, registers it in the "known namespaces" map
     * and creates the corresponding attribute <code>xmlns:prefix="uri"<code>. 
     */
    static String getNsPrefix(String nsUri, Map<String, String> knownNamespaces, Map<String, String> attributes) {
        String prefix = knownNamespaces[nsUri]
        if (nsUri && !prefix) {
            prefix = createNsPrefix(nsUri, knownNamespaces, attributes)
        }
        return (prefix && (prefix != '*')) ? prefix : '' 
    }


    /**
     * Adds the given namespace URI to the "known namespaces" map and creates
     * the corresponding attribute <code>xmlns:prefix="uri"<code>. 
     */
    static String createNsPrefix(String nsUri, Map<String, String> knownNamespaces, Map<String, String> attributes) {
        String prefix = "ns${knownNamespaces.size()}"
        attributes["xmlns:${prefix}"] = nsUri
        prefix += ':'
        knownNamespaces[nsUri] = prefix 
        return prefix
    }
}
