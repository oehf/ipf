package org.openehealth.ipf.platform.camel.ihe.xds.commons.utils;

import static junit.framework.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Test;


public class TestSoapUtils {
    
    @Test
    public void testExctractSoapBody() {
        final String contents = 
            "<ns2:ImportantQuestion>" +
            "Camel rules as cigarettes as well.  Do Apache Indians smoke it?" +
            "</ns2:ImportantQuestion>";

        final String envelopeWithNamespacePrefixes = 
            "<soap:Envelope><soap:Header>" +
            "    <thirdns:header value=\"12345\">some text</thirdns:header></soap:Header>" +
            "    <soap:Body>" + 
            contents + 
            "</soap:Body></soap:Envelope>";

        final String envelopeWithoutNamespacePrefixes = 
            "<Envelope><Header>" +
            "    <thirdns:header value=\"12345\">some text</thirdns:header></Header>" +
            "    <Body>" + 
            contents + 
            "</Body></Envelope>";

        assertEquals(contents, SoapUtils.extractSoapBody(envelopeWithNamespacePrefixes));
        assertEquals(contents, SoapUtils.extractSoapBody(envelopeWithoutNamespacePrefixes));
            
        Assert.assertNull(SoapUtils.extractSoapBody(null));
        Assert.assertNull(SoapUtils.extractSoapBody("12345"));
    }

}
