/*
 * Copyright 2023 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLClassification30;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ClassificationType;

/**
 * @author Dmytro Rud
 */
public class ClassificationListAdapter extends XmlAdapter<ClassificationList, List<EbXMLClassification>> {

    @Override
    public List<EbXMLClassification> unmarshal(ClassificationList classificationList) {
        if (classificationList == null) {
            return null;
        }
        return classificationList.classifications.stream()
                .map(EbXMLClassification30::new)
                .collect(Collectors.toList());
    }

    @Override
    public ClassificationList marshal(List<EbXMLClassification> ebXMLClassifications) {
        if (ebXMLClassifications == null) {
            return null;
        }
        var classifications = new ArrayList<ClassificationType>();
        for (var ebXml : ebXMLClassifications) {
            if (ebXml instanceof EbXMLClassification30 ebxml30) {
                classifications.add(ebxml30.getInternal());
            } else {
                throw new UnsupportedOperationException("Only ebXML 3.0 is supported at the moment");
            }
        }
        var result = new ClassificationList();
        result.setClassifications(classifications);
        return result;
    }
}
