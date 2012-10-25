package org.openehealth.ipf.platform.camel.core.dataformat;

import org.slf4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

class SimpleSaxErrorHandler implements ErrorHandler {

    private Logger log;
    
    SimpleSaxErrorHandler(Logger log) {
        this.log = log;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        log.warn("Ignored XML validation warning", exception);

    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }
    
    @Override
    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }


}
