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
package org.openehealth.ipf.tutorials.xds.store;

import jakarta.activation.DataHandler;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Utility functionality for document content.
 *
 * @author Jens Riemschneider
 */
public final class ContentUtils {

    private ContentUtils() {
    }

    /**
     * @param dataHandler the data handler to access the content
     * @return the size of the content in bytes
     */
    public static long size(DataHandler dataHandler) {
        return getContent(dataHandler).length;
    }

    /**
     * @param dataHandler the data handler to access the content
     * @return the SHA-1 of the content as lower-case hex string
     */
    public static String sha1(DataHandler dataHandler) {
        try {
            var digest = MessageDigest.getInstance("SHA-1");
            return HexFormat.of().formatHex(digest.digest(getContent(dataHandler)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Every Java platform supports SHA-1", e);
        }
    }

    /**
     * @param dataHandler the data handler to access the content
     * @return the content
     */
    public static byte[] getContent(DataHandler dataHandler) {
        try (var inputStream = dataHandler.getInputStream()) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
