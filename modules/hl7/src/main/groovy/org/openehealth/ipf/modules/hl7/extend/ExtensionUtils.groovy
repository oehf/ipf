package org.openehealth.ipf.modules.hl7.extend

import ca.uhn.hl7v2.model.Type

class ExtensionUtils {

    static def normalizeCollection = { Collection c ->
        c.collect { it instanceof Type ? it.encode() : it.toString() };
    }
    
}
