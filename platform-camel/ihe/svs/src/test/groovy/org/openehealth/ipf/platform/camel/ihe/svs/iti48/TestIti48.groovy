package org.openehealth.ipf.platform.camel.ihe.svs.iti48

import org.apache.cxf.binding.soap.SoapFault
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest
import org.openehealth.ipf.commons.ihe.svs.core.requests.ValueSetRequest
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse
import org.openehealth.ipf.platform.camel.ihe.svs.core.converters.SvsConverters
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

import static org.junit.jupiter.api.Assertions.*

/**
 * @author Quentin Ligier
 * */
class TestIti48 extends StandardTestContainer {

    static final String CONTEXT_DESCRIPTOR = 'iti-48.xml'

    final String SERVICE1 = "svs-iti48://localhost:${port}/service1-ok"
    final String SERVICE2 = "svs-iti48://localhost:${port}/service2-exception"

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }

    @BeforeAll
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @Test
    void testIti48() {
        assert auditSender.messages.size() == 0

        def request = SvsConverters.xmlToSvsQuery("""
            <RetrieveValueSetRequest xmlns="urn:ihe:iti:svs:2008">
                <ValueSet id="1.2.840.10008.6.1.308" xml:lang="en-EN"/>
            </RetrieveValueSetRequest>
            """)

        def response = sendIt(SERVICE1, request)
        assert response != null
        assert response.valueSet != null
        assert response.valueSet.id == "1.2.840.10008.6.1.308"
        assert response.valueSet.conceptList.size() == 1
        assert response.valueSet.conceptList[0].concept.size() == 2

        assert auditSender.messages.size() == 2

        SoapFault exception = assertThrows(SoapFault.class, () -> {
            sendIt(SERVICE2, request)
        })
        assert exception != null
        assert exception.message == "Language 'en-EN' not supported"
        assert exception.getFaultCode().localPart == "Sender"
        assert exception.subCode.localPart == "LANGUNK"

        assert auditSender.messages.size() == 4
        for (message in auditSender.messages) {
            assert message.eventIdentification.eventTypeCode[0].code == "ITI-48"
            assert message.eventIdentification.eventTypeCode[0].codeSystemName == "IHE Transactions"
            assert message.eventIdentification.eventTypeCode[0].originalText == "Retrieve Value Set"

            assert message.participantObjectIdentifications[0].participantObjectID == "1.2.840.10008.6.1.308"
        }

        request.valueSet = new ValueSetRequest()
        exception = assertThrows(SoapFault.class, () -> {
            sendIt(SERVICE2, request)
        })
        assert exception != null
        assert exception.getFaultCode().localPart == "Sender"

        assert auditSender.messages.size() == 6
    }

    RetrieveValueSetResponse sendIt(String endpoint, RetrieveValueSetRequest request) {
        return send(endpoint, request, RetrieveValueSetResponse.class)
    }
}
