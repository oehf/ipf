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
package org.openehealth.ipf.commons.core.modules.api;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;

/**
 * @author Dmytro Rud
 */
public class ValidationExceptionTest {

    @Test
    public void testMessages() {
        String s;
        var message = "exception message 12345";
        Throwable[] causes;

        // ----------------- 
        causes = new Throwable[] {new RuntimeException("12345"), new RuntimeException("abcd") };
        
        s = new ValidationException(message, causes).getMessage();
        assertTrue(s.startsWith(message));
        assertTrue(s.contains("\n12345"));
        assertTrue(s.contains("\nabcd"));
        
        s = new ValidationException(causes).getMessage();
        assertTrue(s.startsWith("12345\n"));
        assertTrue(s.endsWith("\nabcd"));

        // -----------------
        causes = new Throwable[] { new RuntimeException(), new RuntimeException() };

        s = new ValidationException(message, causes).getMessage();
        assertTrue(s.startsWith(message));
        assertTrue(s.indexOf("java.lang.RuntimeException") != s.lastIndexOf("java.lang.RuntimeException")); 
        
        s = new ValidationException(causes).getMessage();
        assertEquals("java.lang.RuntimeException\njava.lang.RuntimeException", s); 

        // -----------------
        causes = new Throwable[] { new RuntimeException(), new RuntimeException("67890") };

        s = new ValidationException(message, causes).getMessage();
        assertTrue(s.startsWith(message));
        assertTrue(s.contains("\njava.lang.RuntimeException"));
        assertTrue(s.contains("\n67890"));

        s = new ValidationException(causes).getMessage();
        assertTrue(s.startsWith("java.lang.RuntimeException\n"));
        assertTrue(s.endsWith("\n67890"));

        // -----------------
        causes = new Throwable[] {};
        s = new ValidationException(message, causes).getMessage();
        assertEquals(s, message);

        s = new ValidationException(causes).getMessage();
        assertEquals(s, ValidationException.class.getName());

        // -----------------
        causes = null;
        s = new ValidationException(message, causes).getMessage();
        assertEquals(s, message);

        s = new ValidationException(causes).getMessage();
        assertEquals(s, ValidationException.class.getName());
    
        // -----------------
        Throwable cause = null;
        s = new ValidationException(message, cause).getMessage();
        assertEquals(s, message);

        s = new ValidationException(cause).getMessage();
        assertEquals(s, ValidationException.class.getName());
    }
}
