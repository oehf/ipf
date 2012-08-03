package org.openehealth.ipf.modules.cda;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;

public class CDAR2ValidatorTest {
    private static final Log LOG = LogFactory.getLog(CDAR2ValidatorTest.class.getName());

    CDAR2Validator validator;

    // Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=364797
    static {
        URIConverter.URI_MAP.put(
            URI.createURI("http://www.eclipse.org/ocl/1.1.0/oclstdlib.ecore"),
            URI.createPlatformPluginURI("/org.eclipse.ocl.ecore/model/oclstdlib.ecore", true));
    }

    @Before
    public void setUp() throws Exception {
        validator = new CDAR2Validator();
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public final void validateCDA() throws Exception {
        InputStream is = getClass().getResourceAsStream(
            "/builders/content/document/SampleCDADocument.xml");
        ClinicalDocument cda = CDAUtil.load(is);
        validator.validate(cda, null);
    }
    
    @Test(expected=ValidationException.class)
    public final void validateCDAError() throws Exception {
        InputStream is = getClass().getResourceAsStream(
            "/builders/content/document/InvalidCDADocument.xml");
        ClinicalDocument cda = CDAUtil.load(is);
        validator.validate(cda, null);
    }
    
    @Test
    public final void validateCCD() throws Exception {
        InputStream is = getClass().getResourceAsStream(
            "/builders/content/document/SampleCCDDocument.xml");
        ClinicalDocument ccd = CDAUtil.load(is);
        validator.validate(ccd, null);
    }
    
    @Test(expected = ValidationException.class)
    public final void validateCCDError() throws Exception {
        InputStream is = getClass().getResourceAsStream(
            "/builders/content/document/InvalidCCDDocument.xml");
        ClinicalDocument ccd = CDAUtil.load(is);
        validator.validate(ccd, null);
    }
    
    @Test
    public final void validateCDAwithCCDProfile() throws Exception {
        InputStream is = getClass().getResourceAsStream(
            "/builders/content/document/SampleCDADocument.xml");
        ClinicalDocument ccd = CDAUtil.load(is);
        validator.validate(ccd, null);
    }


}
