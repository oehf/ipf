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
package org.openehealth.ipf.commons.lbs.store;

import static org.apache.commons.lang.Validate.notNull;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Store implementation based on a file system storage.
 * <p>
 * This class is thread-safe
 * @author Jens Riemschneider
 */
public class DiskStore implements LargeBinaryStore {
    private static final URI DEFAULT_BASE_URI = URI.create("lbs://localhost/");
    private static final FlatUriUuidConversion DEFAULT_CONVERSION_STRATEGY = 
        new FlatUriUuidConversion(DEFAULT_BASE_URI);

    private final UuidUriConversionStrategy uuidUriConversion;
    private final FileSystemLayoutStrategy fileSystemLayout;

    private static final Log log = LogFactory.getLog(DiskStore.class);

    /**
     * Constructs a new disk store using the supplied layout and conversion strategies
     * @param fileSystemLayout  
     *          the strategy defining where resources are saved on disk
     * @param uuidUriConversion 
     *          the strategy converting between {@code UUID} and {@code URI}
     * @throws IllegalArgumentException     
     *          if the configuration of the strategies is not correct. This is 
     *          thrown if the location of the file system layout is not suitable 
     *          for the store (e.g. not writable or not a directory).
     */
    public DiskStore(
            FileSystemLayoutStrategy fileSystemLayout,
            UuidUriConversionStrategy uuidUriConversion) {
        
        notNull(uuidUriConversion, "uuidUriConversion cannot be null");
        notNull(fileSystemLayout, "fileSystemLayout cannot be null");

        validateStoreLocation(fileSystemLayout.getBaseDir());
        this.fileSystemLayout = fileSystemLayout;
        this.uuidUriConversion = uuidUriConversion;
        
        StoreRegistration.register(this);
        
        log.debug("Created: " + this);
    }
    
    /**
     * Constructs a new disk store at a given directory.
     * <p>
     * This constructor uses a {@link FlatFileSystemLayout} and a {@link FlatUriUuidConversion}.
     * The directory is created automatically if it does not exist.
     * @param baseDir
     *          the directory where to store the resources
     */
    public DiskStore(String baseDir) {        
        this(new FlatFileSystemLayout(getOrCreateDir(baseDir)),DEFAULT_CONVERSION_STRATEGY);
    }

    private static File getOrCreateDir(String dirPath) {
        File dir = new File(dirPath);
        dir.mkdirs();
        return dir;
    }
    
    @Override
    public URI add(byte[] binary) {
        notNull(binary, "binary cannot be null");
        return add(new ByteArrayInputStream(binary));
    }

    @Override
    public URI add(InputStream binary) {
        notNull(binary, "binary cannot be null");
        return addResource(binary);
    }

    @Override
    public URI add() {
        return add(new byte[] {});
    }
    
    @Override
    public byte[] getByteArray(URI resourceUri) {
        notNull(resourceUri, "resourceUri cannot be null");
        
        InputStream stream = getInputStream(resourceUri);
        try {
            try {
                return IOUtils.toByteArray(stream);
            }
            finally {
                stream.close();
            }
        } catch (IOException e) {
            throw new ResourceIOException(e);
        }
    }

    @Override
    public InputStream getInputStream(URI resourceUri) {
        notNull(resourceUri, "resourceUri cannot be null");
        
        UUID uuid = getMandatoryUuidFromUri(resourceUri);        
        File file = fileSystemLayout.toFile(uuid);
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new ResourceNotFoundException(resourceUri);
        }
    }

    @Override
    public void delete(URI resourceUri) {
        notNull(resourceUri, "resourceUri cannot be null");
        
        File file = getMandatoryFileForResource(resourceUri);
        deleteWithRetry(file);
        
        log.debug("deleted resource: " + resourceUri);
    }

    @Override
    public void deleteAll() {
        deleteDirWithRetry(fileSystemLayout.getBaseDir());
    }

    private void deleteDirWithRetry(File dir) {
        for (int attempt = 1; attempt <= 5; ++attempt) {
            try {
                FileUtils.cleanDirectory(dir);
                return;
            }
            catch (IOException e) {
                if (attempt < 5) {
                    log.debug("Attempt to delete directory '" + dir + "' failed. Retrying deletion.");
                    sleep();
                }
                else {
                    log.warn("Deletion of directory '" + dir.getAbsolutePath() + "' failed due to: " + e.getMessage());
                    throw new ResourceIOException("Could not delete: " + dir, e);
                }
            }
        }
    }

    private void deleteWithRetry(File file) {
        for (int attempt = 1; attempt <= 5; ++attempt) {
            if (file.delete()) {
                return;
            }
            else {
                if (attempt < 5) {
                    log.debug("Attempt to delete file '" + file.getAbsolutePath() + "' failed. Retrying deletion.");
                    sleep();
                }
            }
        }
        
        log.warn("Deletion of file '" + file.getAbsolutePath() + "' failed");
        throw new ResourceIOException("Could not delete: " + file.getAbsolutePath());            
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean contains(URI resourceUri) {
        notNull(resourceUri, "resourceUri cannot be null");
        
        UUID uuid = uuidUriConversion.toUuid(resourceUri);
        if (uuid == null) {
            return false;
        }
        
        File file = fileSystemLayout.toFile(uuid);
        return file.exists();
    }

    @Override
    public long getSize(URI resourceUri) {
        notNull(resourceUri, "resourceUri cannot be null");
        
        File file = getMandatoryFileForResource(resourceUri);
        return file.length();
    }

    @Override
    public OutputStream getOutputStream(URI resourceUri) {
        notNull(resourceUri, "resourceUri cannot be null");
        
        File file = getMandatoryFileForResource(resourceUri);
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new ResourceIOException(e);
        }
    }
    
    @Override
    public String toString() {
        return String.format("{%1$s: fileSystemLayout=%2$s, uuidUriConversion=%3$s}", getClass()
                .getSimpleName(), fileSystemLayout, uuidUriConversion);        
    }
    
    private File getMandatoryFileForResource(URI resourceUri) {
        UUID uuid = getMandatoryUuidFromUri(resourceUri);        
        File file = fileSystemLayout.toFile(uuid);
        if (!file.exists()) {
            throw new ResourceNotFoundException(resourceUri);
        }
        return file;
    }

    private static void validateStoreLocation(File dir) {
        if (!dir.exists()) {
            throw new IllegalArgumentException("Store location does not exist:" + dir);
        }
        
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Store location is not a directory: " + dir);
        }
        
        if (!dir.canWrite()) {
            throw new IllegalArgumentException("Cannot write to store location: " + dir);
        }
    }

    private URI addResource(InputStream binary) {
        UUID uuid = UUID.randomUUID();
        writeToDisk(uuid, binary);
        URI resourceUri = uuidUriConversion.toUri(uuid);
        
        log.debug("added resource: " + resourceUri);
        return resourceUri;
    }
    
    private UUID getMandatoryUuidFromUri(URI resourceUri) {
        UUID uuid = uuidUriConversion.toUuid(resourceUri);
        if (uuid == null) {
            throw new ResourceNotFoundException(resourceUri);
        }
        return uuid;
    }

    private void writeToDisk(UUID uuid, InputStream binary) {
        try {
            File file = getFile(uuid);
            copySafe(binary, file);            
        } catch (IOException e) {
            throw new ResourceIOException(e);
        }
    }

    private File getFile(UUID uuid) {
        File file = fileSystemLayout.toFile(uuid);
        ensureDirectoriesExist(file);
        return file;
    }

    private void ensureDirectoriesExist(File file) {
        File parentDir = file.getParentFile();
        String baseDirPath = fileSystemLayout.getBaseDir().getAbsolutePath();
        baseDirPath = FilenameUtils.normalizeNoEndSeparator(baseDirPath);
        String parentDirPath = parentDir.getAbsolutePath();
        parentDirPath = FilenameUtils.normalizeNoEndSeparator(parentDirPath);
        if (FilenameUtils.equalsOnSystem(parentDirPath, baseDirPath)) {
            return;
        }
        
        if (!parentDir.mkdirs()) {
            throw new ResourceIOException(
                    "Unable to create directory: parentDir=" + parentDirPath + 
                    ", baseDir=" + baseDirPath);
        }
    }

    private void copySafe(InputStream binary, File file) throws IOException {
        BufferedOutputStream output = 
            new BufferedOutputStream(new FileOutputStream(file));
        
        try {
            IOUtils.copy(binary, output);
        }
        finally {
            output.close();
        }
    }
}
