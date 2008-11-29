package org.openehealth.ipf.modules.hl7;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;

public class CompositeHL7v2ExceptionTest {

	private PipeParser p = new PipeParser();

	private ErrorLocation loc1;
	private ErrorLocation loc2;
	private ErrorLocation loc3;
	HL7v2Exception ex1;
	HL7v2Exception ex2;
	HL7v2Exception ex3;

	@Before
	public void setUp() throws Exception {
		loc1 = new ErrorLocation("MSH", 1, 2, 3, 4, 5);
		loc2 = new ErrorLocation("PID", 5, 4, 3, 2, 1);
		loc3 = new ErrorLocation("PV1", 0, 0, 0, 0, 0);
		ex1 = new HL7v2Exception("Blah", 200, null, loc1);
		ex2 = new HL7v2Exception("Bleh", 201, null, loc2);
		ex3 = new HL7v2Exception("Blih", 203, null, loc3);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMultipleSegments_Version23() throws Exception {

		ca.uhn.hl7v2.model.v23.message.ACK response = new ca.uhn.hl7v2.model.v23.message.ACK();

		Terser t = new Terser(response);
		t.set("MSH-1", "|");
		t.set("MSH-2", "^~\\&");
		t.set("MSH-3", "X");
		t.set("MSH-4", "Y");
		t.set("MSH-9-1", "ACK");
		t.set("MSH-9-2", "A08");
		t.set("MSH-10", "1");
		t.set("MSH-12", "2.3");

		CompositeHL7v2Exception ex0 = new CompositeHL7v2Exception("Bluh",
				new HL7v2Exception[] { ex1, ex2, ex3 });
		ex0.populateMessage(response, AckTypeCode.AE);

		String result = p.encode(response);
		//  System.out.println(result);
		assertTrue(result
				.contains("ERR|MSH^1^2^200&Unsupported message type&HL70357&&Blah~PID^5^4^201&Unsupported event code&HL70357&&Bleh~PV1^0^0^203&Unsupported version id&HL70357&&Blih"));
	}                      

	@Test
	public void testMultipleSegments_Version25() throws Exception {

		ca.uhn.hl7v2.model.v25.message.ACK response = new ca.uhn.hl7v2.model.v25.message.ACK();
		Terser t = new Terser(response);
		t.set("MSH-1", "|");
		t.set("MSH-2", "^~\\&");
		t.set("MSH-3", "X");
		t.set("MSH-4", "Y");
		t.set("MSH-9-1", "ACK");
		t.set("MSH-9-2", "A08");
		t.set("MSH-10", "1");
		t.set("MSH-12", "2.5");

		CompositeHL7v2Exception ex0 = new CompositeHL7v2Exception("Bluh",
				new HL7v2Exception[] { ex1, ex2, ex3 });
		ex0.populateMessage(response, AckTypeCode.AE);

		String result = p.encode(response);

		// System.out.println(result);

		assertTrue(result
				.contains("ERR||MSH^1^2^3^4^5~PID^5^4^3^2^1~PV1^0^0^0^0^0|203^Unsupported version id^HL70357^^Blih|E|||Blih"));
	}

}