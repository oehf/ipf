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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openehealth.ipf.commons.lbs.store.DiskStore;
import org.openehealth.ipf.commons.lbs.store.FileSystemLayoutStrategy;
import org.openehealth.ipf.commons.lbs.store.FlatFileSystemLayout;
import org.openehealth.ipf.commons.lbs.store.FlatUriUuidConversion;
import org.openehealth.ipf.commons.lbs.store.ResourceIOException;


/**
 * @author Jens Riemschneider
 */
public class DiskStoreTest extends LargeBinaryStoreTest {
    private File storeLocation;
    private DiskStore diskStore;
    private static final URI baseUri = URI.create("http://localhost/");
    private FlatUriUuidConversion diskStoreStrategy;
    private FlatFileSystemLayout layoutStrategy;
    
    @Before
    public void setUp() throws Exception {
        StoreRegistration.reset();
        
        File temp = File.createTempFile(getClass().getName(), "");
        temp.delete();
        storeLocation = new File(temp.getAbsolutePath());
        storeLocation.mkdir();
        
        diskStoreStrategy = new FlatUriUuidConversion(baseUri);
        layoutStrategy = new FlatFileSystemLayout(storeLocation);
        
        diskStore = new DiskStore(layoutStrategy, diskStoreStrategy);
        setStore(diskStore);
    }
    
    @After
    public void tearDown() throws Exception {
        if (diskStore != null) {
            diskStore.deleteAll();
            if (storeLocation.exists()) {
                storeLocation.setWritable(true);
                FileUtils.deleteDirectory(storeLocation);
            }
        }

        StoreRegistration.reset();
    }
    
    @Test
    public void testConstructorWithNull() throws Exception {
        assertIllegalArg(new Code() { public void run() {
            new DiskStore(layoutStrategy, null);
        }});

        assertIllegalArg(new Code() { public void run() {
            new DiskStore(null, diskStoreStrategy);
        }});
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonExistingLocation() {        
        File nonExistingLocation = new File("/" + UUID.randomUUID().toString());
        FlatFileSystemLayout invalidLayout = 
            new FlatFileSystemLayout(nonExistingLocation);
        
        new DiskStore(invalidLayout, diskStoreStrategy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithFileAsLocation() throws IOException {
        File file = File.createTempFile(getClass().getName(), "txt");
        final FlatFileSystemLayout invalidLayout = 
            new FlatFileSystemLayout(file);
        
        try {
            new DiskStore(invalidLayout, diskStoreStrategy);
        }
        finally {
            file.delete();
        }
    }
     
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithReadOnlyLocation() {
        storeLocation.setWritable(false);
        final FlatFileSystemLayout invalidLayout = 
            new FlatFileSystemLayout(storeLocation);

        try {
            new DiskStore(invalidLayout, diskStoreStrategy);
        }
        finally {
            storeLocation.setWritable(true);
        }
    }
    
    @Ignore
    @Test(expected = ResourceIOException.class)
    public void testDeleteResourceButCannotDeleteFileOnDisk() throws Exception {
        byte[] binary = new byte[] { 0, 1 };
        URI resourceUri = diskStore.add(binary);
        File resource = getFileFromUri(resourceUri);
        
        // Open the file to ensure that it cannot be removed
        InputStream stream = new FileInputStream(resource);
        try {
            diskStore.delete(resourceUri);
        }
        finally {
            stream.close();
        }
    }

    @Ignore
    @Test(expected = ResourceIOException.class)
    public void testDeleteAllResourcesButCannotDeleteFileOnDisk() throws Exception {
        byte[] binary = new byte[] { 0, 1 };
        URI resourceUri = diskStore.add(binary);
        File resource = getFileFromUri(resourceUri);
        
        // Open the file to ensure that it cannot be removed
        InputStream stream = new FileInputStream(resource);
        try {
            diskStore.deleteAll();
        }
        finally {
            stream.close();
        }
    }
    
    private static class SubDirFileSystemLayout extends FileSystemLayoutStrategy {
        public SubDirFileSystemLayout(File baseDirectory) {
            super(baseDirectory);
        }

        @Override
        public File toFile(UUID uuid) {
            String path = FilenameUtils.concat(
                    getBaseDir().getAbsolutePath(), "subdir");
            path = FilenameUtils.concat(path, uuid.toString());
            
            return new File(path);
        }
    }
    
    @Test
    public void testFileSystemLayoutSupportsSubDirectories() {
        SubDirFileSystemLayout subDirFileSystemLayout = 
            new SubDirFileSystemLayout(storeLocation);
        DiskStore testStore = new DiskStore(
                subDirFileSystemLayout, diskStoreStrategy);
        
        URI resourceUri = testStore.add(new byte[] { 1, 2 });
        assertTrue(testStore.contains(resourceUri));
        testStore.delete(resourceUri);
    }
    
    private static class InvalidFileSystemLayout extends FileSystemLayoutStrategy {
        private File file;
        
        public InvalidFileSystemLayout(File baseDir, File file) {
            super(baseDir);
            this.file = file;
        }
        
        @Override
        public File toFile(UUID uuid) {
            return file;
        }
    }
    
    @Test(expected = ResourceIOException.class)
    public void testFileSystemLayoutCreatesInvalidLocation() throws IOException {
        File file = File.createTempFile(getClass().getName(), "txt");
        InvalidFileSystemLayout invalidFileSystemLayout = 
            new InvalidFileSystemLayout(storeLocation, file);
        
        try {        
            DiskStore testStore = new DiskStore(
                    invalidFileSystemLayout, diskStoreStrategy);
            
            testStore.add(new byte[] { 1, 2 });
        }
        finally {        
            file.delete();
        }
    }

    @Test(expected = ResourceIOException.class)
    public void testAddFailsToAccessFile() throws IOException {        
        File file = new File(storeLocation.getAbsolutePath() + "/dir");
        file.mkdirs();
        InvalidFileSystemLayout invalidFileSystemLayout = 
            new InvalidFileSystemLayout(storeLocation, file);
        
        DiskStore testStore = new DiskStore(invalidFileSystemLayout, diskStoreStrategy);        
        testStore.add();
    }
    
    private File getFileFromUri(URI resourceUri) {
        UUID uuid = diskStoreStrategy.toUuid(resourceUri);
        return layoutStrategy.toFile(uuid);
    }
}
