package org.openehealth.ipf.commons.ihe.xds.core.metadata.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;

public class DocumentContentHelperTest {
    private Map<Class<?>, Object> contents = new HashMap<Class<?>, Object>();
    
    @Before
    public void setUp() throws Exception {
        contents.put(DataHandler.class, SampleData.createDataHandler());

    }

    @Test
    public final void testConvertToString() {
        Object result = DocumentContentHelper.convert(contents, String.class);
        assertTrue(result instanceof String);
    }
    
    @Test
    public final void testConvertToDocument() {
        contents.put(String.class, "<root><child id=\"1\">ohovaboho</child></root>");
        Object result = DocumentContentHelper.convert(contents, org.w3c.dom.Document.class);
        assertTrue(result instanceof org.w3c.dom.Document);
    }
    
    @Test
    public final void testConvert() {
        contents.put(String.class, "<test");
        Object result = DocumentContentHelper.convert(contents, org.w3c.dom.Document.class);
        assertNull(result);
    }
    
}
