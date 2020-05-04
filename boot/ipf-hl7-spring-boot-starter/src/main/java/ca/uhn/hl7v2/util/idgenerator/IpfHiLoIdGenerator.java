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

package ca.uhn.hl7v2.util.idgenerator;

import org.openehealth.ipf.boot.hl7v2.IpfHl7v2ConfigurationProperties;

/**
 *
 */
public class IpfHiLoIdGenerator extends DelegatingHiLoGenerator {

    public IpfHiLoIdGenerator(IpfHl7v2ConfigurationProperties.FileIdGeneratorProperties properties) {
        super();
        var generator = new FileBasedGenerator(properties.getLo());
        generator.setDirectory(properties.getDirectory());
        generator.setFileName(properties.getFileMame());
        generator.setMinimizeReads(properties.isMinimizeReads());
        generator.setNeverFail(properties.isNeverFail());
        setDelegate(generator);
    }


}

