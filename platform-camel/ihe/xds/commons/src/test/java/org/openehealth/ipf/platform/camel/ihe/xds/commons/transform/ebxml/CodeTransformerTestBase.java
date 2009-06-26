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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Slot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.CodeTransformer;

/**
 * Tests for {@link CodeTransformer}. 
 * @author Jens Riemschneider
 */
public abstract class CodeTransformerTestBase {
    protected CodeTransformer transformer;
    private Code code;
    
    @Before
    public final void baseSetUp() {
        LocalizedString displayName = new LocalizedString();
        displayName.setCharset("charset");
        displayName.setLang("lang");
        displayName.setValue("value");

        code = new Code();
        code.setCode("code");
        code.setDisplayName(displayName);
        code.setSchemeName("schemeName");
    }
    
    @Test
    public void testToEbXML() {
        Classification ebXML = transformer.toEbXML(code);
        
        assertNotNull(ebXML);
        assertEquals("code", ebXML.getNodeRepresentation());
        
        List<Slot> slots = ebXML.getSlots();
        assertEquals(1, slots.size());
        
        Slot slot = slots.get(0);
        assertEquals(Vocabulary.SLOT_NAME_CODING_SCHEME, slot.getName());        
        assertEquals(Arrays.asList("schemeName"), slot.getValueList());
        
        List<LocalizedString> localizedStrings = ebXML.getNameAsInternationalString().getLocalizedStrings();
        assertEquals(1, localizedStrings.size());
        
        LocalizedString localized = localizedStrings.get(0);
        assertEquals("charset", localized.getCharset());
        assertEquals("lang", localized.getLang());
        assertEquals("value", localized.getValue());
    }
    
    @Test
    public void testToEbXMLNull() {
        assertNull(transformer.toEbXML(null));
    }

    @Test
    public void testToEbXMLEmpty() {
        Classification ebXML = transformer.toEbXML(new Code());
        assertNotNull(ebXML);
        
        assertNull(ebXML.getNodeRepresentation());
        assertNull(ebXML.getName());
        assertEquals(0, ebXML.getSlots().size());
    }
    
    
    @Test
    public void testFromEbXML() {
        Classification ebXML = transformer.toEbXML(code);
        assertEquals(code, transformer.fromEbXML(ebXML));
    }
    
    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.fromEbXML(null));
    }
    
    @Test
    public void testFromEbXmlEmpty() {
        Classification ebXML = transformer.toEbXML(new Code());
        assertEquals(new Code(), transformer.fromEbXML(ebXML));
    }
}
