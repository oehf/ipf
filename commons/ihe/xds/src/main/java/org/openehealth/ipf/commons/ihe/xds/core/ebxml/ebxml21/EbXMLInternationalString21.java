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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLInternationalString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.InternationalStringType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.LocalizedStringType;

/**
 * Encapsulation of {@link InternationalStringType}.
 * @author Jens Riemschneider
 */
public class EbXMLInternationalString21 implements EbXMLInternationalString {
    private final InternationalStringType international;

    /**
     * Constructs the string by wrapping the given ebXML 2.1 object.
     * @param international
     *          the string to wrap. Can be <code>null</code>.
     */
    public EbXMLInternationalString21(InternationalStringType international) {
        this.international = international;
    }

    /**
     * Constructs the string by wrapping the given ebXML 2.1 object.
     * @param localized
     *          the string to wrap. Can be <code>null</code>.
     */
    public EbXMLInternationalString21(LocalizedString localized) {
        if (localized != null) {
            LocalizedStringType localizedEbRS21 = EbXMLFactory21.RIM_FACTORY.createLocalizedStringType();
            localizedEbRS21.setCharset(localized.getCharset());
            localizedEbRS21.setLang(localized.getLang());
            localizedEbRS21.setValue(localized.getValue());

            international = EbXMLFactory21.RIM_FACTORY.createInternationalStringType();
            international.getLocalizedString().add(localizedEbRS21);
        }
        else {
            international = null;
        }
    }

    @Override
    public LocalizedString getLocalizedString(int idx) {
        if (international == null) {
            return null;
        }
        
        List<LocalizedStringType> localizedList = international.getLocalizedString();
        if (idx >= localizedList.size() || idx < 0) {
            return null;
        }
        
        LocalizedStringType localizedEbRS21 = localizedList.get(idx);
        if (localizedEbRS21 == null) {
            return null;
        }
        
        return createLocalizedString(localizedEbRS21);
    }

    @Override
    public List<LocalizedString> getLocalizedStrings() {
        if (international == null) {
            return Collections.emptyList(); 
        }
        
        List<LocalizedStringType> list = international.getLocalizedString();
        List<LocalizedString> result = new ArrayList<LocalizedString>(list.size());
        for (LocalizedStringType localizedEbRS21 : list) {
            result.add(createLocalizedString(localizedEbRS21));
        }
        return result;
    }

    @Override
    public LocalizedString getSingleLocalizedString() {
        if (international == null) {
            return null;
        }
        
        List<LocalizedStringType> locals = international.getLocalizedString();
        if (locals == null || locals.size() == 0) {
            return null;
        }
        
        return createLocalizedString(locals.get(0));
    }

    private LocalizedString createLocalizedString(LocalizedStringType localizedEbRS21) {
        LocalizedString localized = new LocalizedString();
        localized.setCharset(localizedEbRS21.getCharset());
        localized.setLang(localizedEbRS21.getLang());
        localized.setValue(localizedEbRS21.getValue());
        return localized;
    }

    /**
     * @return the ebXML 2.1 object being wrapped by this class.
     */
    InternationalStringType getInternal() {
        return international;
    }
}
