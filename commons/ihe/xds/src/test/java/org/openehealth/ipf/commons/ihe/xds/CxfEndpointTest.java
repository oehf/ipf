package org.openehealth.ipf.commons.ihe.xds;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.Test;import org.openehealth.ipf.commons.ihe.xds.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
import org.openehealth.ipf.commons.ihe.xds.ports.Iti42PortType;
import org.openehealth.ipf.commons.ihe.xds.requests.RegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.responses.ErrorInfo;
import org.openehealth.ipf.commons.ihe.xds.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.server.JettyServer;
import org.openehealth.ipf.commons.ihe.xds.server.ServletServer;
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.transform.requests.RegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.transform.responses.ResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.validate.requests.SubmitObjectsRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.validate.responses.RegistryResponseValidator;
import org.springframework.core.io.ClassPathResource;

public class CxfEndpointTest {
    private final EbXMLFactory factory = new EbXMLFactory30();

    private final RegisterDocumentSetTransformer reqTransformer = 
        new RegisterDocumentSetTransformer(factory);
    
    private final ResponseTransformer respTransformer = 
        new ResponseTransformer(factory);
    
    @Test
    public void test() throws IOException, Exception {
        int port = ServletServer.getFreePort();
        
        JettyServer server = new JettyServer();
        server.setContextResource(new ClassPathResource("cxf-context.xml").getURI().toString());
        server.setPort(port);
        server.setContextPath("");
        server.setServletPath("/*");
        server.setServlet(new CXFServlet());
        
        server.start();
        try {
            ItiServiceFactory serviceFactory = new ItiServiceFactory(ItiServiceInfo.ITI_42, null, "/iti-42");
            ItiClientFactory clientFactory = new ItiClientFactory(ItiServiceInfo.ITI_42, false, null, "http://localhost:" + port + "/iti-42");
            
            serviceFactory.createService(MyIti42.class);
            Iti42PortType client = (Iti42PortType) clientFactory.getClient();
            
            RegisterDocumentSet request = SampleData.createRegisterDocumentSet();
            EbXMLSubmitObjectsRequest ebXMLReq = reqTransformer.toEbXML(request);            
            SubmitObjectsRequest rawReq = (SubmitObjectsRequest) ebXMLReq.getInternal();
            
            RegistryResponseType rawResp = client.documentRegistryRegisterDocumentSetB(rawReq);
            
            EbXMLRegistryResponse30 ebXMLResp = new EbXMLRegistryResponse30(rawResp);
            Response response = respTransformer.fromEbXML(ebXMLResp);
            
            assertEquals(Status.SUCCESS, response.getStatus());
        }
        finally {
            server.stop();
        }
    }
    
    public static class MyIti42 implements Iti42PortType {
        private final EbXMLFactory factory = new EbXMLFactory30();

        private final RegisterDocumentSetTransformer reqTransformer = 
            new RegisterDocumentSetTransformer(factory);
        
        private final ResponseTransformer respTransformer = 
            new ResponseTransformer(factory);
        
        private final SubmitObjectsRequestValidator reqValidator = 
            new SubmitObjectsRequestValidator();

        private final RegistryResponseValidator respValidator = 
            new RegistryResponseValidator();
        
        @Override
        public RegistryResponseType documentRegistryRegisterDocumentSetB(SubmitObjectsRequest rawReq) {
            EbXMLSubmitObjectsRequest30 ebXMLReq = new EbXMLSubmitObjectsRequest30(rawReq);            
            reqValidator.validate(ebXMLReq, null);
            RegisterDocumentSet request = reqTransformer.fromEbXML(ebXMLReq);
            
            Response response = new Response(Status.SUCCESS);
            if (!request.getSubmissionSet().getEntryUuid().equals("submissionSet01")) {
                response.setStatus(Status.FAILURE);
                response.setErrors(Arrays.asList(new ErrorInfo(ErrorCode.REGISTRY_ERROR, "unexpected value", Severity.ERROR, null)));
            }
            
            EbXMLRegistryResponse ebXMLResp = respTransformer.toEbXML(response);
            respValidator.validate(ebXMLResp, null);
            RegistryResponseType rawResp = (RegistryResponseType) ebXMLResp.getInternal();
            return rawResp;
        }
    }
}
