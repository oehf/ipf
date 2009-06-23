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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs30;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.LocalizedStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.SlotType1;

/**
 * Tests for {@link CodeTransformer}. 
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class CodeTransformerTest {
    private CodeTransformer transformer;
    private Code code;
    
    @Before
    public void setUp() {
        transformer = new CodeTransformer();

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
    public void testToEbXML30() {
        ClassificationType ebXML = transformer.toEbXML30(code);
        
        assertNotNull(ebXML);
        assertEquals("code", ebXML.getNodeRepresentation());
        
        List<SlotType1> slots = ebXML.getSlot();
        assertEquals(1, slots.size());
        
        SlotType1 slot = slots.get(0);
        assertEquals(Vocabulary.SLOT_NAME_CODING_SCHEME, slot.getName());        
        assertEquals(Arrays.asList("schemeName"), slot.getValueList().getValue());
        
        List<LocalizedStringType> localizedStrings = ebXML.getName().getLocalizedString();
        assertEquals(1, localizedStrings.size());
        
        LocalizedStringType localized = localizedStrings.get(0);
        assertEquals("charset", localized.getCharset());
        assertEquals("lang", localized.getLang());
        assertEquals("value", localized.getValue());
        
        
    }
    
    @Test
    public void testToEbXML30Null() {
        assertNull(transformer.toEbXML30(null));
    }

    @Test
    public void testToEbXML30Empty() {
        ClassificationType ebXML = transformer.toEbXML30(new Code());
        assertNotNull(ebXML);
        
        assertNull(ebXML.getNodeRepresentation());
        assertNull(ebXML.getName());
        assertEquals(0, ebXML.getSlot().size());
    }
    
    
    @Test
    public void testFromEbXML30() {
        ClassificationType ebXML = transformer.toEbXML30(code);
        Code result = transformer.fromEbXML30(ebXML);
        
        assertEquals(code.getCode(), result.getCode());
        assertEquals(code.getSchemeName(), result.getSchemeName());
        assertEquals(code.getDisplayName().getCharset(), result.getDisplayName().getCharset());
        assertEquals(code.getDisplayName().getLang(), result.getDisplayName().getLang());
        assertEquals(code.getDisplayName().getValue(), result.getDisplayName().getValue());
    }
    
    @Test
    public void testFromEbXML30Null() {
        assertNull(transformer.fromEbXML30(null));
    }
    
    @Test
    public void testFromEbXml30Empty() {
        Code result = transformer.fromEbXML30(new ClassificationType());
        assertNotNull(result);
        assertNull(result.getCode());
        assertNull(result.getSchemeName());
        assertNull(result.getDisplayName());        
    }
}
