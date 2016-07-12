package org.openehealth.ipf.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;

/**
 *
 */
@ConfigurationProperties(prefix = "ipf.hl7")
public class IpfHl7ConfigurationProperties {

    /**
     * Charset used for decoding and encoding HL7 messages
     */
    private String charset = "UTF-8";

    /**
     * Whether to convert line feeds to proper segment separators before parising starts
     */
    private boolean convertLFToCR = false;


    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        // Ensure that charset exists
        Charset.forName(charset);
        this.charset = charset;
    }


    public boolean getConvertLFToCR() {
        return convertLFToCR;
    }

    public void setConvertLFToCR(boolean convertLFToCR) {
        this.convertLFToCR = convertLFToCR;
    }
}
