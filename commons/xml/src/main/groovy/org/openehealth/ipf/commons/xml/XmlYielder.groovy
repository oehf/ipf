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

import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.Node
import groovy.xml.MarkupBuilder

/**
 * Routines for yielding of XML contents from a {@link GPathResult GPath object} 
 * to a {@link MarkupBuilder markup builder}.
 * @author Dmytro Rud
 */
class XmlYielder {
    private static final String DEFAULT_NS_PREFIX = ''
    
    /**
     * Yields the XML element represented by the given GPath result instance 
     * into the given target XML builder, using the given namespace URI  
     * as a default one, i.e. without prefix for it.
     */
    static void yieldElement(GPathResult origin, MarkupBuilder target, String defaultNamespaceUri) {
        if (!origin) {
            return
        }
        yieldElement(origin, origin.nodeIterator().next(), target, [(defaultNamespaceUri) : '*'])
    }
     
     
    /**
     * Yields the given source XML element into the given target XML builder,
     * using the provided set of pre-defined namespace prefixes.
     */
    static void yieldElement(GPathResult origin, Node source, MarkupBuilder target, Map<String, String> predefinedNamespaces) {
        if (!source) {
            return
        }
        
        Map<String, String> attributes = [:]
        Map<String, String> knownNamespaces = [:]
        knownNamespaces.putAll(predefinedNamespaces)

        String elementNsPrefix = getNsPrefix(source.namespaceURI(), knownNamespaces, attributes)
        String namespaceUri = source.namespaceURI();
        
        Map attributeNamespaces = source.attributeNamespaces;
        
        for(Map<String, String> attribute in source.attributes()) {
            String attributeNsUri = lookupNsUri(origin, attributeNamespaces.get(attribute.key))
            
            String attributeNsPrefix
            if (elementNsPrefix && (knownNamespaces[attributeNsUri] == '*')) {
                // attribute with the default namespace inside of an element with a custom namespace 
                attributeNsPrefix = createNsPrefix(attributeNsUri, knownNamespaces, attributes)
            } else {
                attributeNsPrefix = getNsPrefix(attributeNsUri, knownNamespaces, attributes)
            }
            
            String attributeName = attribute.key
            String attributeValue = attribute.value
            
            if (isXSITypeInAttributeValue(attributeNsUri, attributeName)){
               attributeValue = updateNsPrefixInAttributeValue(origin, attributeValue, attributes, knownNamespaces)                                                      
            } 
            
            attributes[attributeNsPrefix + attributeName] = attributeValue
        }

        target."${elementNsPrefix + source.name()}"(attributes) { 
            yieldChildren(origin, source, target, knownNamespaces)
        }
    }
    
    private static String lookupNsUri(GPathResult origin, String attributeNsUri){
        attributeNsUri != null? attributeNsUri: origin.lookupNamespace(DEFAULT_NS_PREFIX);
    }

    /**
     * Updates the namespace prefix in attribute values <br>
     * For example, for attribute value <b>xsi:type="ns1:II", ns1="http:some.namespace.uri"</b>
     * the method returns <b>xsi:type="II"</b>, where <b>"http:some.namespace.uri"</b> is the default namespace.  
     */
    private static String updateNsPrefixInAttributeValue(GPathResult origin, 
                                                 String attributeValue, 
                                                 Map<String, String> attributes, 
                                                 Map<String, String> knownNamespaces){
        def nsPrefixAndType = attributeValue.split(':')
        def nsPrefix = nsPrefixAndType.size() == 1? DEFAULT_NS_PREFIX :  nsPrefixAndType[0]
        def type = nsPrefixAndType.size() == 1? nsPrefixAndType[0] : nsPrefixAndType[1]
        // Use the GPathResult to resolve the namespace uri
        def nsUri = origin.lookupNamespace(nsPrefix)
        
        def newNsPrefix = getNsPrefix(nsUri, knownNamespaces, attributes)
        return newNsPrefix + type
    }
                                                 
    private static Boolean isXSITypeInAttributeValue(String attributeUri, String attributeName){
        return 'http://www.w3.org/2001/XMLSchema-instance' == attributeUri && 
               attributeName == 'type' 
    }
    
    
    /**
     * Yields child elements of the given source XML element into the given target 
     * XML builder, using the provided set of pre-defined namespace prefixes.
     */
    static void yieldChildren(GPathResult origin, Node source, MarkupBuilder target, Map<String, String> predefinedNamespaces) {
        if (!source) {
            return
        }
        for (child in source.children()) {
            if (child instanceof Node) {
                yieldElement(origin, child, target, predefinedNamespaces)
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
    static void yieldChildren(GPathResult origin, MarkupBuilder target, String defaultNamespaceUri) {
        if (!origin) {
            return
        }
        yieldChildren(origin, origin.nodeIterator().next(), target, [(defaultNamespaceUri) : '*'])
    }

     
    /**
     * Returns namespace prefix for the given namespace URI.
     * When this URI is not known yet, registers it in the "known namespaces" map
     * and creates the corresponding attribute <code>xmlns:prefix="uri"</code>. 
     */
    static String getNsPrefix(String nsUri, Map<String, String> knownNamespaces, Map<String, String> attributes) {
        String prefix = knownNamespaces[nsUri]
        if (nsUri && !prefix) {
            prefix = createNsPrefix(nsUri, knownNamespaces, attributes)
        }
        return (prefix && (prefix != '*')) ? prefix : DEFAULT_NS_PREFIX 
    }


    /**
     * Adds the given namespace URI to the "known namespaces" map and creates
     * the corresponding attribute <code>xmlns:prefix="uri"</code>. 
     */
    static String createNsPrefix(String nsUri, Map<String, String> knownNamespaces, Map<String, String> attributes) {
        String prefix = "ns${knownNamespaces.size()}"
        attributes["xmlns:${prefix}"] = nsUri
        prefix += ':'
        knownNamespaces[nsUri] = prefix 
        return prefix
    }
}
