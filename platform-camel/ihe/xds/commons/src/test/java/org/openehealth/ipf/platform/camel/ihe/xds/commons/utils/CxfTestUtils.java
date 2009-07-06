/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.utils;

import org.apache.commons.io.IOUtils;

import javax.activation.DataHandler;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * Provides a few utility functions for stream-related tests.
 */
public abstract class CxfTestUtils {
    private CxfTestUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Determines if a data handler is using MTOM.
     * <p>
     * The input stream must have been retrieved using a data handler that was 
     * created via CXFs standard attachment interceptors.
     * <p>
     * The implementation of this method uses internal knowledge of how MTOM is
     * supported within CXF. This is faster and safer than reading the whole
     * stream (assuming it wouldn't fit into memory if MTOM isn't used).
     * @param inputStream
     *          the input stream to check. Must have been retrieved from a data
     *          handler that was obtained via CXF marshaling.
     * @return {@code true} if the handler was created using the CXF MTOM support.
     */
    public static boolean isCxfUsingMtom(InputStream inputStream) {
        boolean isMtom = true;
        try {
            Class<? extends InputStream> aClass = inputStream.getClass();
            Field field = aClass.getDeclaredField("is");
            field.setAccessible(true);
            FileInputStream fileInputStream = (FileInputStream) field.get(inputStream);            
            if (!fileInputStream.getFD().valid()) {
                isMtom = false;
            }
        }
        catch (Exception e) {
            isMtom = false;
        }
        return isMtom;
    }
}
