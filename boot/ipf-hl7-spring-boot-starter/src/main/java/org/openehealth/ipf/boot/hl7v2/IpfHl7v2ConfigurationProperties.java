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

import ca.uhn.hl7v2.util.Home;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

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
    @Getter @Setter
    private boolean caching = false;

    /**
     * Whether ID generator to use. One of "file" (default), "uuid", "nano". Alternatively, you can
     * provide your own bean of type {@link ca.uhn.hl7v2.util.idgenerator.IDGenerator}.
     */
    @Getter @Setter
    private String generator = "file";

    @Getter
    @NestedConfigurationProperty
    private final FileIdGeneratorProperties idGenerator = new FileIdGeneratorProperties();

    public static class FileIdGeneratorProperties  {

        /**
         * How many IDs to be generated internally before incrementing the file value
         */
        @Getter @Setter private int lo = 100;

        /**
         * Directory of the ID file
         */
        @Getter @Setter private String directory = Home.getHomeDirectory().getAbsolutePath();

        /**
         * Name of the ID file
         */
        @Getter @Setter private String fileMame = "id_file";

        /**
         * If set to <code>false</code> (default is <code>true</code>),
         * retrieving a new ID may fail if the ID file in the home
         * directory can not be written/read. If set to true, failures
         * will be ignored, which means that IDs may be repeated after
         * a JVM restart.
         */
        @Getter @Setter private boolean neverFail = true;

        /**
         * If set to <code>true</code> (default is <code>false</code>) the generator
         * minimizes the number of disk reads by caching the last read value. This means
         * one less disk read per X number of IDs generated, but also means that multiple
         * instances of this generator may clobber each other's values.
         */
        @Getter @Setter private boolean minimizeReads = false;
    }

}
