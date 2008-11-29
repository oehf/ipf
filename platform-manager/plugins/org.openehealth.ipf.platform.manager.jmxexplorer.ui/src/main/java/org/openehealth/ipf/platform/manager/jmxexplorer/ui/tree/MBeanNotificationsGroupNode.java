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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree;

import org.openehealth.ipf.platform.manager.connection.ui.tree.Node;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.Messages;

/**
 * Node which visualizes (represents) a group of MBean Notifications MBean
 * 
 * @author Mitko Kolev
 */
public class MBeanNotificationsGroupNode extends Node {

    /**
     * @param name
     */
    public MBeanNotificationsGroupNode() {
        super(Messages.getLabelString("mbean.notifications"));
    }
}