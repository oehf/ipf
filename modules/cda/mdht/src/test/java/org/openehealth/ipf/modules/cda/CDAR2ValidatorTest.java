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
package org.openehealth.ipf.modules.cda;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CDAR2ValidatorTest {
    private static final Logger LOG = LoggerFactory.getLogger(CDAR2ValidatorTest.class.getName());

    private CDAR2Validator validator;

    // Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=364797
    static {
        URIConverter.URI_MAP.put(
            URI.createURI("http://www.eclipse.org/ocl/1.1.0/oclstdlib.ecore"),
            URI.createPlatformPluginURI("/org.eclipse.ocl.ecore/model/oclstdlib.ecore", true));
    }

    @BeforeEach
    public void setUp() throws Exception {
        validator = new CDAR2Validator();
        var context = new HashMap<Object, Object>();
        context.put(CDAUtil.ValidationHandler.class, new DefaultValidationHandler());
        CDAR2Utils.initCCD();
        CDAR2Utils.initHITSPC32();

    }

    @Test
    public final void validateCDA() throws Exception {
        LOG.info("Validating plain CDA document");
        var is = getClass().getResourceAsStream(
            "/builders/content/document/SampleCDADocument.xml");
        var cda = CDAUtil.load(is);
        validator.validate(cda, null);
    }
    
    @Test
    public final void validateCDAError() throws Exception {
        LOG.info("Validating erroneous plain CDA document");
        var is = getClass().getResourceAsStream(
            "/builders/content/document/InvalidCDADocument.xml");
        var cda = CDAUtil.load(is);
        assertThrows(ValidationException.class, () -> validator.validate(cda, null));
        // TODO check expected validation errors
    }
    
    @Test
    public final void validateCCD() throws Exception {
        LOG.info("Validating CCD document");
        var is = getClass().getResourceAsStream(
            "/builders/content/document/SampleCCDDocument.xml");
        var ccd = CDAUtil.load(is);
        validator.validate(ccd, null);
    }
    
    @Test
    public final void validateCCDError() throws Exception {
        LOG.info("Validating erroneous CCD document");
        var is = getClass().getResourceAsStream(
            "/builders/content/document/InvalidCCDDocument.xml");
        var ccd = CDAUtil.load(is);
        assertThrows(ValidationException.class, () -> validator.validate(ccd, null));
        // TODO check expected validation errors
    }

    @Disabled
    public final void validateCDAwithHITSPProfile() throws Exception {
        LOG.info("Validating HITSPC32 document");
        var is = getClass().getResourceAsStream(
                "/builders/content/document/SampleHITSPC32v25Document.xml");
        var hitsp = CDAUtil.load(is);
        validator.validate(hitsp, null);
    }
}
