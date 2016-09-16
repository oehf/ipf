/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.boot.hl7v2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;

/**
 *
 */
@ConfigurationProperties(prefix = "ipf.hl7v2")
public class IpfHl7v2ConfigurationProperties {

    /**
     * Charset used for decoding and encoding HL7 messages
     */
    @Getter
    private String charset = "UTF-8";

    /**
     * Whether to convert line feeds to proper segment separators before parsing starts
     */
    @Getter @Setter
    private boolean convertLinefeed = false;

    public void setCharset(String charset) {
        // Ensure that charset exists
        Charset.forName(charset);
        this.charset = charset;
    }

    /**
     * Whether to instantiate a continuation cache
     */
    @Getter
    @Setter
    private boolean caching = false;
}
