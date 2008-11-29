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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.builder.strategy;

import java.util.List;

/**
 * Strategy for grouping MBeans in the tree. In some cases a different ordering
 * would be more helpful.
 * 
 * @author Mitko Kolev
 */
public abstract class NodeOrderStrategyAbstract {
    /**
     * Defines the order in which the MBean properties should be returned to the
     * builder
     * 
     * @param propertiesString
     * @return
     */
    public abstract List<String> parseProperties(String propertiesString);
}
