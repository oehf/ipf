/*
 * Copyright 2016 the original author or authors.
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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.enumfactories.*;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Dmytro Rud
 */
abstract public class XdsEnumAdapter<T extends XdsEnum> extends XmlAdapter<XdsEnumJaxbElement, T> {

    private final XdsEnumFactory<T> factory;

    public XdsEnumAdapter(XdsEnumFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public T unmarshal(XdsEnumJaxbElement jaxb) throws Exception {
        if (jaxb == null) {
            return null;
        }

        switch (jaxb.getType()) {
            case INVALID:
                return factory.createCode(XdsEnum.Type.INVALID, jaxb.getValue());
            case USER_DEFINED:
                XdsEnum.Type type = factory.canBeUserDefined(jaxb.getValue())
                        ? XdsEnum.Type.USER_DEFINED
                        : XdsEnum.Type.INVALID;
                return factory.createCode(type, jaxb.getValue());
            default:
                for (T official : factory.getOfficialValues()) {
                    if (official.getJaxbValue().equals(jaxb.getValue())) {
                        return official;
                    }
                }
                return factory.fromEbXML(jaxb.getValue());
        }
    }

    @Override
    public XdsEnumJaxbElement marshal(T xds) throws Exception {
        if (xds == null) {
            return null;
        }

        XdsEnumJaxbElement jaxb = new XdsEnumJaxbElement();
        jaxb.setType(xds.getType());
        jaxb.setValue(xds.getJaxbValue());
        return jaxb;
    }



    public static class AssociationLabelAdapter extends XdsEnumAdapter<AssociationLabel> {
        public AssociationLabelAdapter() {
            super(new AssociationLabelFactory());
        }
    }

    public static class AssociationTypeAdapter extends XdsEnumAdapter<AssociationType> {
        public AssociationTypeAdapter() {
            super(new AssociationTypeFactory30());
        }
    }

    public static class AvailabilityStatusAdapter extends XdsEnumAdapter<AvailabilityStatus> {
        public AvailabilityStatusAdapter() {
            super(new AvailabilityStatusFactory30());
        }
    }

    public static class AvailabilityStatusForQueryAdapter extends XdsEnumAdapter<AvailabilityStatus> {
        public AvailabilityStatusForQueryAdapter() {
            super(new AvailabilityStatusForQueryFactory());
        }
    }

    public static class DocumentAvailabilityAdapter extends XdsEnumAdapter<DocumentAvailability> {
        public DocumentAvailabilityAdapter() {
            super(new DocumentAvailabilityFactory());
        }
    }

    public static class DocumentEntryTypeAdapter extends XdsEnumAdapter<DocumentEntryType> {
        public DocumentEntryTypeAdapter() {
            super(new DocumentEntryTypeFactory());
        }
    }

    public static class ErrorCodeAdapter extends XdsEnumAdapter<ErrorCode> {
        public ErrorCodeAdapter() {
            super(new ErrorCodeFactory());
        }
    }

    public static class QueryReturnTypeAdapter extends XdsEnumAdapter<QueryReturnType> {
        public QueryReturnTypeAdapter() {
            super(new QueryReturnTypeFactory());
        }
    }

    public static class QueryTypeAdapter extends XdsEnumAdapter<QueryType> {
        public QueryTypeAdapter() {
            super(new QueryTypeFactory());
        }
    }

    public static class ReferenceIdTypeAdapter extends XdsEnumAdapter<ReferenceIdType> {
        public ReferenceIdTypeAdapter() {
            super(new ReferenceIdTypeFactory());
        }
    }

    public static class SeverityAdapter extends XdsEnumAdapter<Severity> {
        public SeverityAdapter() {
            super(new SeverityFactory30());
        }
    }

    public static class StatusAdapter extends XdsEnumAdapter<Status> {
        public StatusAdapter() {
            super(new StatusFactory30());
        }
    }

}
