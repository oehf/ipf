/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Dmytro Rud
 */
public class PatientInfoAdapter extends XmlAdapter<PatientInfoXml, PatientInfo> {

    @Override
    public PatientInfoXml marshal(PatientInfo patientInfo) throws Exception {
        if (patientInfo == null) {
            return null;
        }

        PatientInfoXml xml = new PatientInfoXml();
        marshalList(patientInfo.getIds(), xml.getIds());
        marshalList(patientInfo.getNames(), xml.getNames());
        xml.setDateOfBirth(patientInfo.getDateOfBirth());
        xml.setGender(patientInfo.getGender());
        marshalList(patientInfo.getAddresses(), xml.getAddresses());
        patientInfo.getCustomFieldIds().forEach(fieldId -> {
            List<String> target = xml.getOther().computeIfAbsent(fieldId, dummy -> new ArrayList<>());
            marshalList(patientInfo.getHl7FieldIterator(fieldId), target);
        });
        return xml;
    }

    @Override
    public PatientInfo unmarshal(PatientInfoXml xml) throws Exception {
        if (xml == null) {
            return null;
        }

        PatientInfo patientInfo = new PatientInfo();
        unmarshalList(xml.getIds(), patientInfo.getIds());
        unmarshalList(xml.getNames(), patientInfo.getNames());
        patientInfo.setDateOfBirth(xml.getDateOfBirth());
        patientInfo.setGender(xml.getGender());
        unmarshalList(xml.getAddresses(), patientInfo.getAddresses());
        xml.getOther().forEach((fieldId, repetitions) -> {
            unmarshalList(repetitions, patientInfo.getHl7FieldIterator(fieldId));
        });
        return patientInfo;
    }

    private static <T> void marshalList(ListIterator<T> source, List<T> target) {
        while (source.hasNext()) {
            target.add(source.next());
        }
    }

    private static <T> void unmarshalList(List<T> source, ListIterator<T> target) {
        for (int i = source.size() - 1; i >= 0; --i) {
            target.add(source.get(i));
        }
    }

}
