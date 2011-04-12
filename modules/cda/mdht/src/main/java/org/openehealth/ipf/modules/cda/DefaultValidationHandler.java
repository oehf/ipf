package org.openehealth.ipf.modules.cda;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.common.util.Diagnostic;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil.ValidationHandler;

/**
 * Validation Handler
 * @author Stefan Ivanov
 *
 */
public class DefaultValidationHandler implements ValidationHandler {
    private static final Log LOG = LogFactory.getLog(DefaultValidationHandler.class.getName());
    
    @Override
    public void handleError(Diagnostic diagnostic) {
        LOG.error("Validation error:" + diagnostic);
        
    }
    
    @Override
    public void handleWarning(Diagnostic diagnostic) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void handleInfo(Diagnostic diagnostic) {
        // TODO Auto-generated method stub
        
    }
    
}
