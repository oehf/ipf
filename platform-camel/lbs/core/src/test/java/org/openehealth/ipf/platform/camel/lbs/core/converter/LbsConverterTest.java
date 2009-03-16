/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.lbs.core.converter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Arrays;

import javax.activation.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openehealth.ipf.commons.lbs.utils.CorruptedInputStream;
import org.openehealth.ipf.commons.lbs.utils.NiceClass;
import org.openehealth.ipf.commons.lbs.utils.TextInputStream;
import org.openehealth.ipf.platform.camel.lbs.core.converter.LbsConverter;

/**
 * @author Jens Riemschneider
 */
public class LbsConverterTest {
    @Test
    public void testConversionDataSource2InputStream() throws Exception {
        TextInputStream inputStream = new TextInputStream();        
        DataSource dataSource = createMock(DataSource.class);
        expect(dataSource.getInputStream()).andReturn(inputStream);
        replay(dataSource);

        CamelContext camelContext = new DefaultCamelContext();
        DefaultExchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(dataSource);
        InputStream resultInputStream = exchange.getIn().getBody(InputStream.class);
        assertFalse(inputStream.isClosed());
        assertEquals("Hello World", IOUtils.toString(resultInputStream));
        
        verify(dataSource);
    }

    @Test
    public void testConversionDataSource2String() throws Exception {
        TextInputStream inputStream = new TextInputStream();        
        DataSource dataSource = createMock(DataSource.class);
        expect(dataSource.getInputStream()).andReturn(inputStream);
        replay(dataSource);

        CamelContext camelContext = new DefaultCamelContext();
        DefaultExchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(dataSource);
        String result = exchange.getIn().getBody(String.class);
        assertTrue(inputStream.isClosed());
        assertEquals("Hello World", result);

        verify(dataSource);
    }

    @Test(expected = RuntimeCamelException.class)
    public void testConversionDataSource2StringThrowsException() throws Exception {
        CorruptedInputStream inputStream = new CorruptedInputStream();        
        DataSource dataSource = createMock(DataSource.class);
        expect(dataSource.getInputStream()).andReturn(inputStream);
        replay(dataSource);

        CamelContext camelContext = new DefaultCamelContext();
        DefaultExchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(dataSource);
        try {
            exchange.getIn().getBody(String.class);
        }
        finally {
            assertTrue(inputStream.isClosed());
            verify(dataSource);
        }
    }

    @Test
    public void testConversionDataSource2ByteArray() throws Exception {
        TextInputStream inputStream = new TextInputStream();        
        DataSource dataSource = createMock(DataSource.class);
        expect(dataSource.getInputStream()).andReturn(inputStream);
        replay(dataSource);

        CamelContext camelContext = new DefaultCamelContext();
        DefaultExchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(dataSource);
        byte[] result = exchange.getIn().getBody(byte[].class);
        assertTrue(inputStream.isClosed());
        assertTrue(Arrays.equals("Hello World".getBytes(), result));
        verify(dataSource);
    }

    @Test(expected = RuntimeCamelException.class)
    public void testConversionDataSource2ByteArrayThrowsException() throws Exception {
        CorruptedInputStream inputStream = new CorruptedInputStream();        
        DataSource dataSource = createMock(DataSource.class);
        expect(dataSource.getInputStream()).andReturn(inputStream);
        replay(dataSource);

        CamelContext camelContext = new DefaultCamelContext();
        DefaultExchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(dataSource);
        try {
            exchange.getIn().getBody(byte[].class);
        }
        finally {
            assertTrue(inputStream.isClosed());
            verify(dataSource);
        }
    }

    @Test
    public void testNiceClass() throws Exception {
        NiceClass.checkUtilityClass(LbsConverter.class);
    }
}
