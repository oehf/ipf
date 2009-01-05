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
import java.util.UUID;

import org.junit.Test;
import org.openehealth.ipf.commons.lbs.store.FlatFileSystemLayout;


/**
 * @author Jens Riemschneider
 */
public class FlatFileSystemLayoutTest {
    @Test
    public void testSimpleCorrectCase() {
        File base = new File("//sumpf//");
        FlatFileSystemLayout strategy = new FlatFileSystemLayout(base);
        UUID uuid = UUID.randomUUID();
        File file = strategy.toFile(uuid);
        assertEquals(base.getAbsolutePath(), file.getParentFile().getAbsolutePath());
    }

    @Test
    public void testNoPathSeparatorAtEnd() {
        File base = new File("//sumpf//");
        FlatFileSystemLayout strategy = new FlatFileSystemLayout(base);
        UUID uuid = UUID.randomUUID();
        File file = strategy.toFile(uuid);
        assertEquals(base.getAbsolutePath(), file.getParentFile().getAbsolutePath());
    }

    @Test
    public void testEmptyBase() {
        File base = new File("");
        FlatFileSystemLayout strategy = new FlatFileSystemLayout(base);
        UUID uuid = UUID.randomUUID();
        File file = strategy.toFile(uuid);
        assertEquals(base.getAbsolutePath(), file.getParentFile().getAbsolutePath());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullSafetyToFile() {
        new FlatFileSystemLayout(new File("")).toFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSafetyConstructor() {
        new FlatFileSystemLayout(null);
    }
}
