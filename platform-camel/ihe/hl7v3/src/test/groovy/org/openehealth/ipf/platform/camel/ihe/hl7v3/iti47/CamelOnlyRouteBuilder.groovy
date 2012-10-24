package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti47

import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelTranslators.translatorHL7v2toHL7v3;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelTranslators.translatorHL7v3toHL7v2;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti47RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti47ResponseValidator;
import static org.openehealth.ipf.platform.camel.ihe.mllp.PixPdqCamelValidators.iti21RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.mllp.PixPdqCamelValidators.iti21ResponseValidator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder
import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.commons.ihe.hl7v3.translation.PdqRequest3to2Translator;
import org.openehealth.ipf.commons.ihe.hl7v3.translation.PdqResponse2to3Translator;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.ihe.mllp.iti21.Iti21Component;

import ca.uhn.hl7v2.parser.Parser;

class CamelOnlyRouteBuilder extends SpringRouteBuilder {

    private static final Parser PARSER = Iti21Component.CONFIGURATION.getParser();
    private static final PdqRequest3to2Translator REQUEST_TRANSLATOR = new PdqRequest3to2Translator();
    private static final PdqResponse2to3Translator RESPONSE_TRANSLATOR = new PdqResponse2to3Translator();


    @Override
    public void configure() throws Exception {
        from("pdqv3-iti47:iti47Service")
            .onException(Exception.class)
                .maximumRedeliveries(0)
                .end()
            .process(iti47RequestValidator())
            .setHeader("myHeader", constant("content-1"))
            .convertBodyTo(byte[].class)
            .process(translatorHL7v3toHL7v2(REQUEST_TRANSLATOR))
            .process(typeAndHeaderChecker(MessageAdapter.class, "content-1"))
            .process(iti21RequestValidator())
            .setBody(constant(MessageAdapters.make(PARSER, Testiti47CamelOnly.getResponseMessage())))
            .process(iti21ResponseValidator())
            .setHeader("myHeader", constant("content-2"))
            .process(translatorHL7v2toHL7v3(RESPONSE_TRANSLATOR))
            .process(typeAndHeaderChecker(String.class, "content-2"))
            .process(iti47ResponseValidator());
    }
    
    
    private static Processor typeAndHeaderChecker(
            final Class<?> expectedClass,
            final String expectedHeaderContent)
    {
        Validate.notNull(expectedClass);
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Class<?> actualClass = exchange.getIn().getBody().getClass();
                if (! expectedClass.equals(actualClass)) {
                    throw new RuntimeException("Wrong body class: expected " + 
                            expectedClass + ", got " + actualClass);
                }

                if (expectedHeaderContent != null) {
                    String actualHeaderContent = exchange.getIn().getHeader("myHeader", String.class);
                    if (! expectedHeaderContent.equals(actualHeaderContent)) {
                        throw new RuntimeException("wrong headers");
                    }
                }
            }
        };
    }

}
