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

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

/**
 * File system layout strategy that places all files into the base directory.
 * @author Jens Riemschneider
 */
public class FlatFileSystemLayout extends FileSystemLayoutStrategy {
    /**
     * Constructs the layout strategy by defining the base directory.
     * @param baseDir   
     *          see {@link FileSystemLayoutStrategy#FileSystemLayoutStrategy(File)}
     *          for details 
     */
    public FlatFileSystemLayout(File baseDir) {
        super(baseDir);
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.commons.core.lbs.FileSystemLayoutStrategy#toFile(java.util.UUID)
     */
    @Override
    public File toFile(UUID uuid) {
        notNull(uuid, "uuid cannot be null");
        
        String path = FilenameUtils.concat(
                getBaseDir().getAbsolutePath(), uuid.toString());
        
        return new File(path);
    }
}
