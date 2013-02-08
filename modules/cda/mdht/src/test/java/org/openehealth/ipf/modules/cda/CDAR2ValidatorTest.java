package org.openehealth.ipf.modules.cda;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;

public class CDAR2ValidatorTest {
    private static final Logger LOG = LoggerFactory.getLogger(CDAR2ValidatorTest.class.getName());

    private CDAR2Validator validator;
    private Map<Object, Object> context;

    // Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=364797
    static {
        URIConverter.URI_MAP.put(
            URI.createURI("http://www.eclipse.org/ocl/1.1.0/oclstdlib.ecore"),
            URI.createPlatformPluginURI("/org.eclipse.ocl.ecore/model/oclstdlib.ecore", true));
    }

    @Before
    public void setUp() throws Exception {
        validator = new CDAR2Validator();
        context = new HashMap<Object, Object>();
        context.put(CDAUtil.ValidationHandler.class, new DefaultValidationHandler());
        CDAR2Utils.initCCD();
        CDAR2Utils.initHITSPC32();

    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public final void validateCDA() throws Exception {
        LOG.info("Validating plain CDA document");
        InputStream is = getClass().getResourceAsStream(
            "/builders/content/document/SampleCDADocument.xml");
        ClinicalDocument cda = CDAUtil.load(is);
        validator.validate(cda, null);
    }
    
    @Test(expected=ValidationException.class)
    public final void validateCDAError() throws Exception {
        LOG.info("Validating erroneous plain CDA document");
        InputStream is = getClass().getResourceAsStream(
            "/builders/content/document/InvalidCDADocument.xml");
        ClinicalDocument cda = CDAUtil.load(is);
        validator.validate(cda, null);
        // TODO check expected validation errors
    }
    
    @Test
    public final void validateCCD() throws Exception {
        LOG.info("Validating CCD document");
        InputStream is = getClass().getResourceAsStream(
            "/builders/content/document/SampleCCDDocument.xml");
        ClinicalDocument ccd = CDAUtil.load(is);
        validator.validate(ccd, null);
    }
    
    @Test(expected = ValidationException.class)
    public final void validateCCDError() throws Exception {
        LOG.info("Validating erroneous CCD document");
        InputStream is = getClass().getResourceAsStream(
            "/builders/content/document/InvalidCCDDocument.xml");
        ClinicalDocument ccd = CDAUtil.load(is);
        validator.validate(ccd, null);
        // TODO check expected validation errors
    }

    @Ignore
    public final void validateCDAwithHITSPProfile() throws Exception {
        LOG.info("Validating HITSPC32 document");
        InputStream is = getClass().getResourceAsStream(
                "/builders/content/document/SampleHITSPC32v25Document.xml");
        ClinicalDocument hitsp = CDAUtil.load(is);
        validator.validate(hitsp, null);
    }
}
