/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds;

import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.xds.XCMU;
import org.openehealth.ipf.commons.ihe.xds.XDM;
import org.openehealth.ipf.commons.ihe.xds.XDR;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.*;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.RemoveObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.AdhocQueryRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.ProvideAndRegisterDocumentSetRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.RemoveMetadataRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.NonconstructiveDocumentSetRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.RetrieveImagingDocumentSetRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.SubmitObjectsRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.QueryResponseValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.RegistryResponseValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.RetrieveDocumentSetResponseValidator;

import static org.openehealth.ipf.commons.ihe.xds.RAD.Interactions.RAD_69;
import static org.openehealth.ipf.commons.ihe.xds.RAD.Interactions.RAD_75;
import static org.openehealth.ipf.commons.ihe.xds.XCA.Interactions.ITI_38;
import static org.openehealth.ipf.commons.ihe.xds.XCA.Interactions.ITI_39;
import static org.openehealth.ipf.commons.ihe.xds.XCF.Interactions.ITI_63;
import static org.openehealth.ipf.commons.ihe.xds.XDS_B.Interactions.*;
import static org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter.validationEnabled;

/**
 * Validating processors for ebXML 3.0-based and (by inheritance)
 * ebXML 2.1-based IPF XDS components.
 *  
 * @author Dmytro Rud
 */
abstract public class XdsCamelValidators extends XdsACamelValidators {
    
    private static final Processor ITI_18_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLAdhocQueryRequest30 message =
            new EbXMLAdhocQueryRequest30(exchange.getIn().getBody(AdhocQueryRequest.class));
        new AdhocQueryRequestValidator().validate(message, ITI_18);
    };
   
    private static final Processor ITI_18_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLQueryResponse30 message =
            new EbXMLQueryResponse30(exchange.getIn().getBody(AdhocQueryResponse.class));
        new QueryResponseValidator().validate(message, ITI_18);
    };
    
    private static final Processor ITI_38_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLAdhocQueryRequest30 message =
            new EbXMLAdhocQueryRequest30(exchange.getIn().getBody(AdhocQueryRequest.class));
        new AdhocQueryRequestValidator().validate(message, ITI_38);
    };

    private static final Processor ITI_38_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLQueryResponse30 message =
            new EbXMLQueryResponse30(exchange.getIn().getBody(AdhocQueryResponse.class));
        new QueryResponseValidator().validate(message, ITI_38);
    };

    private static final Processor ITI_39_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLNonconstructiveDocumentSetRequest30<RetrieveDocumentSetRequestType> message =
            new EbXMLNonconstructiveDocumentSetRequest30<>(exchange.getIn().getBody(RetrieveDocumentSetRequestType.class));
        new NonconstructiveDocumentSetRequestValidator().validate(message, ITI_39);
    };

    private static final Processor ITI_39_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRetrieveDocumentSetResponse30 message =
            new EbXMLRetrieveDocumentSetResponse30(exchange.getIn().getBody(RetrieveDocumentSetResponseType.class));
        new RetrieveDocumentSetResponseValidator().validate(message, ITI_39);
    };

    private static final Processor ITI_41_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLProvideAndRegisterDocumentSetRequest30 message =
                new EbXMLProvideAndRegisterDocumentSetRequest30(exchange.getIn().getBody(ProvideAndRegisterDocumentSetRequestType.class));
        new ProvideAndRegisterDocumentSetRequestValidator().validate(message, ITI_41);
    };

    private static final Processor ITI_41_XDM_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLProvideAndRegisterDocumentSetRequest30 message =
                new EbXMLProvideAndRegisterDocumentSetRequest30(exchange.getIn().getBody(ProvideAndRegisterDocumentSetRequestType.class));
        new ProvideAndRegisterDocumentSetRequestValidator().validate(message, XDM.Interactions.ITI_41);
    };

    private static final Processor ITI_41_XDR_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLProvideAndRegisterDocumentSetRequest30 message =
                new EbXMLProvideAndRegisterDocumentSetRequest30(exchange.getIn().getBody(ProvideAndRegisterDocumentSetRequestType.class));
        new ProvideAndRegisterDocumentSetRequestValidator().validate(message, XDR.Interactions.ITI_41);
    };

    private static final Processor ITI_41_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRegistryResponse30 message =
            new EbXMLRegistryResponse30(exchange.getIn().getBody(RegistryResponseType.class));
        new RegistryResponseValidator().validate(message, ITI_41);
    };

    private static final Processor ITI_42_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLSubmitObjectsRequest30 message =
            new EbXMLSubmitObjectsRequest30(exchange.getIn().getBody(SubmitObjectsRequest.class));
        new SubmitObjectsRequestValidator().validate(message, ITI_42);
    };
   
    private static final Processor ITI_42_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRegistryResponse30 message = new EbXMLRegistryResponse30(exchange.getIn().getBody(RegistryResponseType.class));
        new RegistryResponseValidator().validate(message, ITI_42);
    };
    
    private static final Processor ITI_43_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLNonconstructiveDocumentSetRequest30<RetrieveDocumentSetRequestType> message =
            new EbXMLNonconstructiveDocumentSetRequest30<>(exchange.getIn().getBody(RetrieveDocumentSetRequestType.class));
        new NonconstructiveDocumentSetRequestValidator().validate(message, ITI_43);
    };
   
    private static final Processor ITI_43_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRetrieveDocumentSetResponse30 message =
            new EbXMLRetrieveDocumentSetResponse30(exchange.getIn().getBody(RetrieveDocumentSetResponseType.class));
        new RetrieveDocumentSetResponseValidator().validate(message, ITI_43);
    };

    private static final Processor ITI_51_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLAdhocQueryRequest30 message =
                new EbXMLAdhocQueryRequest30(exchange.getIn().getBody(AdhocQueryRequest.class));
        new AdhocQueryRequestValidator().validate(message, ITI_51);
    };

    private static final Processor ITI_51_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLQueryResponse30 message =
                new EbXMLQueryResponse30(exchange.getIn().getBody(AdhocQueryResponse.class));
        new QueryResponseValidator().validate(message, ITI_51);
    };

    private static final Processor ITI_57_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLSubmitObjectsRequest30 message =
                new EbXMLSubmitObjectsRequest30(exchange.getIn().getBody(SubmitObjectsRequest.class));
        new SubmitObjectsRequestValidator().validate(message, ITI_57);
    };

    private static final Processor ITI_57_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRegistryResponse30 message = new EbXMLRegistryResponse30(exchange.getIn().getBody(RegistryResponseType.class));
        new RegistryResponseValidator().validate(message, ITI_57);
    };

    private static final Processor ITI_61_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLSubmitObjectsRequest30 message =
            new EbXMLSubmitObjectsRequest30(exchange.getIn().getBody(SubmitObjectsRequest.class));
        new SubmitObjectsRequestValidator().validate(message, ITI_61);
    };

    private static final Processor ITI_61_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRegistryResponse30 message = new EbXMLRegistryResponse30(exchange.getIn().getBody(RegistryResponseType.class));
        new RegistryResponseValidator().validate(message, ITI_61);
    };

    private static final Processor ITI_62_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRemoveMetadataRequest30 message =
                new EbXMLRemoveMetadataRequest30(exchange.getIn().getBody(RemoveObjectsRequest.class));
        new RemoveMetadataRequestValidator().validate(message, ITI_62);
    };

    private static final Processor ITI_62_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRegistryResponse30 message = new EbXMLRegistryResponse30(exchange.getIn().getBody(RegistryResponseType.class));
        new RegistryResponseValidator().validate(message, ITI_62);
    };

    private static final Processor ITI_63_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLAdhocQueryRequest30 message =
            new EbXMLAdhocQueryRequest30(exchange.getIn().getBody(AdhocQueryRequest.class));
        new AdhocQueryRequestValidator().validate(message, ITI_63);
    };

    private static final Processor ITI_63_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLQueryResponse30 message =
            new EbXMLQueryResponse30(exchange.getIn().getBody(AdhocQueryResponse.class));
        new QueryResponseValidator().validate(message, ITI_63);
    };

    private static final Processor ITI_86_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLNonconstructiveDocumentSetRequest30<RemoveDocumentsRequestType> message =
                new EbXMLNonconstructiveDocumentSetRequest30<>(exchange.getIn().getBody(RemoveDocumentsRequestType.class));
        new NonconstructiveDocumentSetRequestValidator().validate(message, ITI_86);
    };

    private static final Processor ITI_86_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRegistryResponse30 message =
                new EbXMLRegistryResponse30(exchange.getIn().getBody(RegistryResponseType.class));
        new RegistryResponseValidator().validate(message, ITI_86);
    };

    private static final Processor CH_XCMU_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLSubmitObjectsRequest30 message =
                new EbXMLSubmitObjectsRequest30(exchange.getIn().getBody(SubmitObjectsRequest.class));
        new SubmitObjectsRequestValidator().validate(message, XCMU.Interactions.CH_XCMU);
    };

    private static final Processor CH_XCMU_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRegistryResponse30 message = new EbXMLRegistryResponse30(exchange.getIn().getBody(RegistryResponseType.class));
        new RegistryResponseValidator().validate(message, XCMU.Interactions.CH_XCMU);
    };

    /**
     * Returns a validating processor for ITI-18 request messages.
     */
    public static Processor iti18RequestValidator() {
        return ITI_18_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-18 response messages.
     */
    public static Processor iti18ResponseValidator() {
        return ITI_18_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-38 request messages.
     */
    public static Processor iti38RequestValidator() {
        return ITI_38_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-38 response messages.
     */
    public static Processor iti38ResponseValidator() {
        return ITI_38_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-39 request messages.
     */
    public static Processor iti39RequestValidator() {
        return ITI_39_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-39 response messages.
     */
    public static Processor iti39ResponseValidator() {
        return ITI_39_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-41 request messages used in the profile XDS.b.
     */
    public static Processor iti41RequestValidator() {
        return ITI_41_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-41 request messages used in the profile XDM.
     */
    public static Processor iti41XdmRequestValidator() {
        return ITI_41_XDM_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-41 request messages used in the profile XDR.
     */
    public static Processor iti41XdrRequestValidator() {
        return ITI_41_XDR_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-41 response messages used in the profiles XDS.b, XDM, XDR.
     */
    public static Processor iti41ResponseValidator() {
        return ITI_41_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-42 request messages.
     */
    public static Processor iti42RequestValidator() {
        return ITI_42_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-42 response messages.
     */
    public static Processor iti42ResponseValidator() {
        return ITI_42_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-43 request messages.
     */
    public static Processor iti43RequestValidator() {
        return ITI_43_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-43 response messages.
     */
    public static Processor iti43ResponseValidator() {
        return ITI_43_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-51 request messages.
     */
    public static Processor iti51RequestValidator() {
        return ITI_51_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-51 response messages.
     */
    public static Processor iti51ResponseValidator() {
        return ITI_51_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-57 request messages.
     */
    public static Processor iti57RequestValidator() {
        return ITI_57_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-57 response messages.
     */
    public static Processor iti57ResponseValidator() {
        return ITI_57_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-61 request messages.
     */
    public static Processor iti61RequestValidator() {
        return ITI_61_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-61 response messages.
     */
    public static Processor iti61ResponseValidator() {
        return ITI_61_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-62 response messages.
     */
    public static Processor iti62RequestValidator() {
        return ITI_62_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-62 response messages.
     */
    public static Processor iti62ResponseValidator() {
        return ITI_62_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-63 request messages.
     */
    public static Processor iti63RequestValidator() {
        return ITI_63_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-63 response messages.
     */
    public static Processor iti63ResponseValidator() {
        return ITI_63_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-86 request messages.
     */
    public static Processor iti86RequestValidator() {
        return ITI_86_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-86 response messages.
     */
    public static Processor iti86ResponseValidator() {
        return ITI_86_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for CH-XCMU request messages.
     */
    public static Processor chXcmuRequestValidator() {
        return CH_XCMU_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for CH-XCMU response messages.
     */
    public static Processor chXcmuResponseValidator() {
        return CH_XCMU_RESPONSE_VALIDATOR;
    }

    private static final Processor RAD_69_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRetrieveImagingDocumentSetRequest30 message =
            new EbXMLRetrieveImagingDocumentSetRequest30(exchange.getIn().getBody(RetrieveImagingDocumentSetRequestType.class));
        new RetrieveImagingDocumentSetRequestValidator().validate(message, RAD_69);
    };

    private static final Processor RAD_69_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRetrieveDocumentSetResponse30 message =
            new EbXMLRetrieveDocumentSetResponse30(exchange.getIn().getBody(RetrieveDocumentSetResponseType.class));
        new RetrieveDocumentSetResponseValidator().validate(message, RAD_69);
    };

    private static final Processor RAD_75_REQUEST_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRetrieveImagingDocumentSetRequest30 message =
            new EbXMLRetrieveImagingDocumentSetRequest30(exchange.getIn().getBody(RetrieveImagingDocumentSetRequestType.class));
        new RetrieveImagingDocumentSetRequestValidator().validate(message, RAD_75);
    };

    private static final Processor RAD_75_RESPONSE_VALIDATOR = exchange -> {
        if (! validationEnabled(exchange)) {
            return;
        }
        EbXMLRetrieveDocumentSetResponse30 message =
            new EbXMLRetrieveDocumentSetResponse30(exchange.getIn().getBody(RetrieveDocumentSetResponseType.class));
        new RetrieveDocumentSetResponseValidator().validate(message, RAD_75);
    };

    /**
     * Returns a validating processor for RAD-69 request messages.
     *
     * @return RAD_69_REQUEST_VALIDATOR
     */
    public static Processor rad69RequestValidator() {
        return RAD_69_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for RAD-69 response messages.
     *
     * @return RAD_69_RESPONSE_VALIDATOR
     */
    public static Processor rad69ResponseValidator() {
        return RAD_69_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for RAD-75 request messages.
     *
     * @return RAD_75_REQUEST_VALIDATOR
     */
    public static Processor rad75RequestValidator() {
        return RAD_75_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for RAD-75 response messages.
     *
     * @return RAD_75_RESPONSE_VALIDATOR
     */
    public static Processor rad75ResponseValidator() {
        return RAD_75_RESPONSE_VALIDATOR;
    }
}
