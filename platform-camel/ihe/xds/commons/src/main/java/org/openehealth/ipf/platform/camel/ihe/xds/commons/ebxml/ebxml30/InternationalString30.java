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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.InternationalString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.InternationalStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.LocalizedStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory;

/**
 * Encapsulation of {@link InternationalStringType}.
 * @author Jens Riemschneider
 */
public class InternationalString30 implements InternationalString {
    private final static ObjectFactory rimFactory = new ObjectFactory();
    
    private final InternationalStringType international;
    
    private InternationalString30(InternationalStringType international) {
        this.international = international;
    }

    static InternationalString30 create(InternationalStringType international) {
        return new InternationalString30(international);
    }
    
    static InternationalString30 create(LocalizedString localized) {
        if (localized == null) {
            return new InternationalString30(null);
        }
        
        LocalizedStringType localizedEbRS30 = rimFactory.createLocalizedStringType();
        localizedEbRS30.setCharset(localized.getCharset());
        localizedEbRS30.setLang(localized.getLang());
        localizedEbRS30.setValue(localized.getValue());

        InternationalStringType international = rimFactory.createInternationalStringType();
        international.getLocalizedString().add(localizedEbRS30);
        return new InternationalString30(international);
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
        
        LocalizedStringType localizedEbRS30 = localizedList.get(idx);
        if (localizedEbRS30 == null) {
            return null;
        }
        
        return createLocalizedString(localizedEbRS30);
    }

    @Override
    public List<LocalizedString> getLocalizedStrings() {
        if (international == null) {
            return Collections.emptyList(); 
        }
        
        List<LocalizedStringType> list = international.getLocalizedString();
        List<LocalizedString> result = new ArrayList<LocalizedString>(list.size());
        for (LocalizedStringType localizedEbRS30 : list) {
            result.add(createLocalizedString(localizedEbRS30));
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

    private LocalizedString createLocalizedString(LocalizedStringType localizedEbRS30) {
        LocalizedString localized = new LocalizedString();
        localized.setCharset(localizedEbRS30.getCharset());
        localized.setLang(localizedEbRS30.getLang());
        localized.setValue(localizedEbRS30.getValue());
        return localized;
    }

    InternationalStringType getInternal() {
        return international;
    }
}
