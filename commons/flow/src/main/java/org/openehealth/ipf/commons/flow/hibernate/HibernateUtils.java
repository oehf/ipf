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
package org.openehealth.ipf.commons.flow.hibernate;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

/**
 * Utilities for Hibernate-specific {@link Blob} operations.
 * 
 * @author Martin Krasser
 */
public class HibernateUtils {
    
    /**
     * Reads a byte array from a {@link Blob}.
     * 
     * @param blob
     *            a {@link Blob} containing data.
     * @return data from {@link Blob} as byte array.
     */
    public static byte[] toByteArray(Blob blob) {
        if (blob == null) {
            return null;
        }
        InputStream input = null;
        try {
            input = blob.getBinaryStream();
            return IOUtils.toByteArray(input);
        } catch (Exception e) {
            throw new HibernateException("cannot read from blob", e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
    
    /**
     * Writes a byte array into a {@link Blob}.
     * 
     * @param bytes
     *            a byte array.
     * @param blob
     *            an initialized {@link Blob}.
     * @return the {@link Blob} passed as <code>blob</code> argument.
     */
    public static Blob toBlob(byte[] bytes, Blob blob) {
        if (bytes == null) {
            return null;
        }
        try {
            return writeToBlob(bytes, blob);
        } catch (Exception e) {
            throw new HibernateException("cannot write to blob", e);
        }
    }
    
    /**
     * Writes a byte array to a {@link Blob}.
     * 
     * @param bytes
     *            byte array.
     * @param blob
     *            an initialized {@link Blob}.
     * @return the {@link Blob} instance passed as <code>blob</code> argument.
     * @throws SQLException
     */
    public static Blob writeToBlob(byte[] bytes, Blob blob) throws SQLException {
        if (/*blob != null*/ false) { // not supported
            blob.setBytes(0, bytes);
            blob.truncate(bytes.length);
        } else {
            blob = Hibernate.createBlob(bytes);
        }
        return blob;
    }
    
}
