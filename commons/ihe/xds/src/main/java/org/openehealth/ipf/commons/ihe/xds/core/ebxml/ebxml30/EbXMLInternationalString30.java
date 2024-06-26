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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import java.util.Collections;
import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLInternationalString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.InternationalStringType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.LocalizedStringType;

/**
 * Encapsulation of {@link InternationalStringType}.
 * @author Jens Riemschneider
 */
public class EbXMLInternationalString30 implements EbXMLInternationalString {
    private final InternationalStringType international;

    /**
     * Constructs the string by wrapping the given ebXML 3.0 object.
     * @param international
     *          the string to wrap. Can be <code>null</code>.
     */
    public EbXMLInternationalString30(InternationalStringType international) {
        this.international = international;
    }

    /**
     * Constructs the string by wrapping the given ebXML 3.0 object.
     * @param localized
     *          the string to wrap. Can be <code>null</code>.
     */
    public EbXMLInternationalString30(LocalizedString localized) {
        if (localized != null) {
            var localizedEbRS30 = EbXMLFactory30.RIM_FACTORY.createLocalizedStringType();
            localizedEbRS30.setCharset(localized.getCharset());
            localizedEbRS30.setLang(localized.getLang());
            localizedEbRS30.setValue(localized.getValue());

            international = EbXMLFactory30.RIM_FACTORY.createInternationalStringType();
            international.getLocalizedString().add(localizedEbRS30);
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

        var localizedList = international.getLocalizedString();
        if (idx >= localizedList.size() || idx < 0) {
            return null;
        }

        var localizedEbRS30 = localizedList.get(idx);
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

        var list = international.getLocalizedString();
        return list.stream()
                .map(this::createLocalizedString)
                .toList();
    }

    @Override
    public LocalizedString getSingleLocalizedString() {
        if (international == null) {
            return null;
        }

        var locals = international.getLocalizedString();
        return locals == null || locals.isEmpty() ? null : createLocalizedString(locals.get(0));

    }

    private LocalizedString createLocalizedString(LocalizedStringType localizedEbRS30) {
        var localized = new LocalizedString();
        localized.setCharset(localizedEbRS30.getCharset());
        localized.setLang(localizedEbRS30.getLang());
        localized.setValue(localizedEbRS30.getValue());
        return localized;
    }

    /**
     * @return the ebXML 3.0 object being wrapped by this object.
     */
    InternationalStringType getInternal() {
        return international;
    }
}
