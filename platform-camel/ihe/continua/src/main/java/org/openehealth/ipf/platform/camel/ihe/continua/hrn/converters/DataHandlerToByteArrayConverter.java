/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.continua.hrn.converters;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;

import javax.activation.DataHandler;
import java.io.IOException;

/**
 * @author Stefan Ivanov
 */
public class DataHandlerToByteArrayConverter implements Converter<DataHandler, byte[]> {
    private final static transient Log LOG = LogFactory.getLog(DataHandlerToByteArrayConverter.class);

    @Override
    public byte[] convert(DataHandler source) {
        try {
            return IOUtils.toByteArray(source.getInputStream());
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }
    }
}
