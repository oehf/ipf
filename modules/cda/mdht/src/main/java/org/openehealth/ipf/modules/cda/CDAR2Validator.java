package org.openehealth.ipf.modules.cda;

import java.util.Map;

import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openhealthtools.mdht.uml.cda.CDAPackage;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.ccd.CCDPackage;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil.ValidationHandler;

/**
 * Validates a ClinicalDocuments instance for conformity against CDA and/or CCD
 * schema.
 * 
 * @author Stefan Ivanov
 * 
 */
public class CDAR2Validator implements Validator<ClinicalDocument, Map<Object, Object>> {
    
    static {
        @SuppressWarnings("unused")
        CDAPackage cdaPackageInstance = CDAPackage.eINSTANCE;
        @SuppressWarnings("unused")
        CCDPackage ccdPackageInstance = CCDPackage.eINSTANCE;
    }

    @Override
    public void validate(ClinicalDocument doc, Map<Object, Object> context) {
        
        boolean isValid = CDAUtil.validate(doc, retrieveValidationHandler(context));
        if (!(Boolean) isValid) {
            throw new ValidationException("Clinical Document not valid!");
        }

    }
    
    /**
     * Retrieves validation handler from context. If none found returns a
     * default validation handler.
     * 
     * @param context
     * @return
     */
    private ValidationHandler retrieveValidationHandler(Map<Object, Object> context) {
        if (context != null) {
            ValidationHandler handler = (ValidationHandler) context.get(ValidationHandler.class);
            if (handler != null) {
                return handler;
            }
        }
        return new DefaultValidationHandler();
    }

}
