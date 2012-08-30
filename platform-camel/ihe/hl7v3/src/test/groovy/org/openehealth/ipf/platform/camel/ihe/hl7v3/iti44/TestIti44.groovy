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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti44

import static org.junit.Assert.*

import org.apache.camel.Consumer
import org.apache.camel.Route
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.*
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import org.apache.camel.ExchangePattern
import org.apache.camel.Processor
import org.apache.camel.Exchange

/**
 * Tests for ITI-44.
 * @author Dmytro Rud
 */
class TestIti44 extends StandardTestContainer {

    def REQUEST2 = '''<?xml version="1.0" encoding="UTF-8"?>
<PRPA_IN201301UV02 ITSVersion="XML_1.0" xmlns:urn="urn:hl7-org:v3" xmlns="urn:hl7-org:v3">
    <id root="2.16.840.1.113883.3.37.4.1.1.2.511.4" extension="1803694657" />
    <creationTime value="20110927143331"/>
    <interactionId extension="PRPA_IN201301UV02" root="2.16.840.1.113883.1.6"/>
    <processingCode code="P"/>
    <processingModeCode code="T"/>
    <acceptAckCode code="AL"/>
    	 <receiver typeCode="RCV">
		<device classCode="DEV" determinerCode="INSTANCE">
		  <id root="1.2.840.114350.1.13.99999.4567"/>
		</device>
	</receiver>
    	<sender typeCode="SND">
		<device classCode="DEV" determinerCode="INSTANCE">
        	<id root="2.16.840.1.113883.3.37.4.1.1.2" extension="511"/>
			<asAgent classCode="AGNT">
				<representedOrganization determinerCode="INSTANCE" classCode="ORG">
					<id root="2.16.840.1.113883.3.37.4.1.1.1.101.1" extension="A5"/>
				</representedOrganization>
			</asAgent>
    	</device>
	</sender>
    <controlActProcess classCode="CACT" moodCode="EVN">
        <code code="PRPA_TE201301UV02" codeSystem="2.16.840.1.113883.1.6"/>
        <subject typeCode="SUBJ">
              <registrationEvent classCode="REG" moodCode="EVN">
                <id nullFlavor="NA"/>
                <statusCode code="active"/>
                <subject1 typeCode="SBJ">
                	<patient classCode="PAT">
                    	<id root="2.16.840.1.113883.3.37.4.1.1.2.511.1" extension="78124" />
                        <statusCode code="active"/>
                                    			<patientPerson>
              				<name use="L">
								<family>Mustermann</family>
								<family qualifier="BR">Mustermann</family>
								<given>Max</given>
								<prefix>Dr.</prefix>
							</name>
							<telecom use="H" value="tel:+49341362542"/>
							<administrativeGenderCode code="M" codeSystem="2.16.840.1.113883.5.1"/>
							<birthTime value="19530101"/>
							<addr use="H">
								<country>DE</country>
								<state></state>
								<city>Hannover</city>
								<postalCode>54000</postalCode>
								<streetAddressLine>Musterstraße 24</streetAddressLine>
							</addr>
							<maritalStatusCode code="S" codeSystem="2.16.840.1.113883.5.2"/>
				        	<religiousAffiliationCode code="1022"/>
            			</patientPerson>
                      							<providerOrganization classCode="ORG" determinerCode="INSTANCE">
							<id extension="PKL" root="2.16.840.1.113883.3.37.4.1.1.1"/>
				            <contactParty classCode="CON"/>
						</providerOrganization>
                    </patient>
                </subject1>
                				<custodian typeCode="CST">
					<assignedEntity classCode="ASSIGNED">
						<id root="2.16.840.1.113883.3.37.4.1.1.2" extension="511"/>
					</assignedEntity>
				</custodian>
              </registrationEvent>
           </subject>
    </controlActProcess>
</PRPA_IN201301UV02>

'''

    def static CONTEXT_DESCRIPTOR = 'iti-44.xml'
    
    def SERVICE1_PIX = "pixv3-iti44://localhost:${port}/pixv3-iti44-service1";
    def SERVICE1_XDS = "xds-iti44://localhost:${port}/xds-iti44-service1";

    private static final String ADD_REQUEST =
            readFile('translation/pixfeed/v3/PIX_FEED_REG_Maximal_Request.xml')
    private static final String REVISE_REQUEST =
            readFile('translation/pixfeed/v3/PIX_FEED_REV_Maximal_Request.xml')
    private static final String MERGE_REQUEST =
            readFile('translation/pixfeed/v3/PIX_FEED_MERGE_Maximal_Request.xml')


    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void setUpClass() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }


    /**
     * Before starting this method, set JVM parameter "-Dfile.encoding=windows-1251",
     * because the second parameter to getBytes() would not have any effect.
     */
    @Test
    @Ignore
    void testMpiProblem() {
        Processor processor = [process : { Exchange arg0 ->
                arg0.in.body = new String(REQUEST2.getBytes('ISO-8859-1'))
                arg0.setProperty(Exchange.CHARSET_NAME, "UTF-8");
        } ] as Processor

        producerTemplate.send(SERVICE1_PIX, ExchangePattern.InOut, processor)
    }


    @Test
    void testIti44Add() {
        def response = send(SERVICE1_XDS, ADD_REQUEST, String.class)
        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            assert it.toString().contains('EventActionCode="C"')
            assert it.toString().contains('EventOutcomeIndicator="0"')
        }
    }
    
    @Test
    void testIti44Revise() {
        def response = send(SERVICE1_XDS, REVISE_REQUEST, String.class)
        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            assert it.toString().contains('EventActionCode="U"')
            assert it.toString().contains('EventOutcomeIndicator="0"')
        }
    }

    @Test
    void testIti44Merge() {
        def response = send(SERVICE1_XDS, MERGE_REQUEST, String.class)
        assert auditSender.messages.size() == 4
        int updateCount = 0
        int deleteCount = 0
        auditSender.messages.each {
            if (it.toString().contains('EventActionCode="U"')) {
                ++updateCount
            } else if (it.toString().contains('EventActionCode="D"')) {
                ++deleteCount
            }
            assert it.toString().contains('EventOutcomeIndicator="0"')
        }
        assert updateCount == 2
        assert deleteCount == 2
    }


    @Test @Ignore
    void testRestart() {
        Consumer consumer = ((Route) camelContext.routes.find {
            it.consumer.endpoint.endpointUri == 'pixv3-iti44://pixv3-iti44-service1'
        }).consumer
        
        // consumer is not yet stopped and should be still able to serve requests
        send(SERVICE1_PIX, '<PRPA_IN201301UV02 xmlns="urn:hl7-org:v3"/>', String.class)
        
        // stop and check whether actually deactivated
        consumer.stop()
        boolean failed = false
        try {
            send(SERVICE1_PIX, '<PRPA_IN201301UV02  xmlns="urn:hl7-org:v3"/>', String.class)
        } catch (Exception e) {
            failed = true
        }
        assertTrue('it should be unable to send a message to stopped consumer', failed)
        
        // restart and check whether actually reactivated
        consumer.start()
        send(SERVICE1_PIX, '<PRPA_IN201301UV02  xmlns="urn:hl7-org:v3"/>', String.class)
    }
    
}
