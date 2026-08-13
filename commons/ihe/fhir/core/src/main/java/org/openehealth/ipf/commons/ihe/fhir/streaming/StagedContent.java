/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.streaming;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Document content that has been extracted from a FHIR request body while that body was still being read
 * from the wire. So, neither the base64 text nor the decoded byte array is ever materialized on the
 * heap by the FHIR parser.
 * <p>
 * Content up to {@code memoryThresholdBytes} is kept in memory, anything beyond that is spilled into a
 * temporary file. Either way the content is handed over to the XDS side as a {@link DataHandler} that e.g. CXF
 * can stream into an MTOM attachment. Large documents never require a contiguous heap allocation.
 * <p>
 * An instance may own a temporary file and must therefore be {@link #close() closed}. The owning
 * {@link StagedContentRegistry} does that at the end of the request.
 *
 * @see StagedContentRegistry
 */
public final class StagedContent implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(StagedContent.class);

    private static final String DIGEST_ALGORITHM = "SHA-1";

    private static final byte[] EMPTY = new byte[0];

    /**
     * the registry-unique token that identifies this content in the request body marker
     */
    @Getter
    private final String token;

    private final int memoryThresholdBytes;
    private final long maxSizeBytes;
    private final Path tempDirectory;
    private final MessageDigest digest;
    private final byte[] singleByte = new byte[1];

    /**
     * Content held in memory, allocated once at its full size on first use rather than grown into: a buffer that
     * doubles its way up to the threshold allocates roughly twice as much again in intermediate arrays.
     */
    private byte[] memory;
    private int memorySize;
    private Path file;
    private OutputStream fileStream;
    private boolean released;

    /**
     * the number of decoded content bytes
     */
    @Getter
    private long size;

    /**
     *  The lower-case hex encoded SHA-1 of the decoded content, computed while it was streamed past. Available once the sink has been
     *  closed. This is the digest XDS expects in, so it can be used to verify a client-supplied without a second pass over the content.
     */
    @Getter
    private String sha1Hex;

    StagedContent(String token, int memoryThresholdBytes, long maxSizeBytes, Path tempDirectory) {
        this.token = token;
        this.memoryThresholdBytes = memoryThresholdBytes;
        this.maxSizeBytes = maxSizeBytes;
        this.tempDirectory = tempDirectory;
        try {
            this.digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            // SHA-1 is required to be present on every JVM
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns the sink to decode the base64 content into. Closing the returned stream does not release the
     * staged content, it only signals that no further content will be written.
     *
     * @return output stream accepting the decoded document content
     */
    public OutputStream sink() {
        return new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                singleByte[0] = (byte) b;
                append(singleByte, 0, 1);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                append(b, off, len);
            }

            @Override
            public void close() throws IOException {
                finish();
            }
        };
    }

    private void append(byte[] b, int off, int len) throws IOException {
        if (size + len > maxSizeBytes) {
            throw new DocumentTooLargeException(maxSizeBytes);
        }
        digest.update(b, off, len);
        size += len;
        if (fileStream == null && size > memoryThresholdBytes) {
            spill();
        }
        if (fileStream != null) {
            fileStream.write(b, off, len);
        } else {
            if (memory == null) {
                memory = new byte[memoryThresholdBytes];
            }
            // Guaranteed to fit: not having spilled means size, and therefore memorySize + len, is within the
            // threshold
            System.arraycopy(b, off, memory, memorySize, len);
            memorySize += len;
        }
    }

    private void spill() throws IOException {
        // Files.createTempFile creates the file with owner-only permissions on POSIX systems
        file = Files.createTempFile(tempDirectory, "fhir-document-", ".bin");
        fileStream = new BufferedOutputStream(Files.newOutputStream(file), 32 * 1024);
        if (memory != null) {
            fileStream.write(memory, 0, memorySize);
            memory = null;
            memorySize = 0;
        }
        log.debug("Document binary content exceeded {} bytes, spilling to {}", memoryThresholdBytes, file);
    }

    private void finish() throws IOException {
        if (sha1Hex != null) {
            return;
        }
        if (fileStream != null) {
            fileStream.close();
            fileStream = null;
        }
        sha1Hex = Hex.encodeHexString(digest.digest());
    }

    /**
     * @return true if the content was large enough to be spilled into a temporary file
     */
    public boolean isSpilled() {
        return file != null;
    }

    /**
     * Exposes this content as a {@link DataSource} for the XDS request. The content type is not known at
     * staging time (in FHIR JSON, {@code contentType} may appear after {@code data}) and is therefore
     * supplied by the caller.
     *
     * @param contentType MIME type of the document
     * @return data source streaming this content
     */
    public DataSource dataSource(String contentType) {
        return new StagedDataSource(contentType);
    }

    /**
     * @param contentType MIME type of the document
     * @return data handler streaming this content
     * @see #dataSource(String)
     */
    public DataHandler dataHandler(String contentType) {
        return new DataHandler(dataSource(contentType));
    }

    @Override
    public void close() {
        try {
            if (fileStream != null) {
                fileStream.close();
                fileStream = null;
            }
        } catch (IOException e) {
            log.warn("Could not close staged binary content {}", file, e);
        }
        memory = null;
        released = true;
        if (file != null) {
            try {
                Files.deleteIfExists(file);
            } catch (IOException e) {
                log.warn("Could not delete staged binary content {}", file, e);
            }
            file = null;
        }
    }

    private class StagedDataSource implements DataSource {

        private final String contentType;

        StagedDataSource(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (file != null) {
                return Files.newInputStream(file);
            }
            if (released) {
                throw new IOException("Staged content " + token + " has already been released");
            }
            // A view onto the buffer rather than a copy of it; memory stays null for zero-length content
            return new ByteArrayInputStream(memory == null ? EMPTY : memory, 0, memorySize);
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Staged content is read-only");
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public String getName() {
            return token;
        }
    }

    /**
     * Thrown when a single document exceeds the configured maximum size.
     */
    public static class DocumentTooLargeException extends IOException {

        public DocumentTooLargeException(long maxSizeBytes) {
            super("Document content exceeds the configured maximum of " + maxSizeBytes + " bytes");
        }
    }
}
