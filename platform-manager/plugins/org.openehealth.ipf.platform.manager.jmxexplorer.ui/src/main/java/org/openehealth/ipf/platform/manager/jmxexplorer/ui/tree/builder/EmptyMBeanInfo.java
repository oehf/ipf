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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.builder;

import javax.management.MBeanInfo;

/**
 * Replaces the actual MBean, when a runtime exception occurs when the MBeanInfo
 * of the MBean is not available.
 * 
 * @author Mitko Kolev
 */
public class EmptyMBeanInfo extends MBeanInfo {
    private static final long serialVersionUID = -4383640300597477162L;

    /**
     * @param className
     *            The class Name of the original MBeanInfo
     * @param description
     *            The description of this empty MBeanInfo
     * @throws IllegalArgumentException
     */
    public EmptyMBeanInfo(String className, String description)
            throws IllegalArgumentException {
        super(className, description, null, null, null, null);

    }

}
