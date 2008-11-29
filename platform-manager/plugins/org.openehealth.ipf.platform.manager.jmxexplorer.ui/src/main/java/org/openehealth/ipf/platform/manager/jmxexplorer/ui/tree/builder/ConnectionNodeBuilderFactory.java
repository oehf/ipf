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

import org.eclipse.core.runtime.Preferences;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.preferences.PreferencesInitializer;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.builder.strategy.NodeOrderByCanonicalRepresentationStrategy;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.builder.strategy.NodeOrderByCreationalOrderStrategy;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.builder.strategy.NodeOrderByTypeStrategy;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.builder.strategy.NodeOrderStrategyAbstract;

/**
 * Reads the settings in the UI configuration and decides how to represent the
 * MBeans in the tree.
 * 
 * @author Mitko Kolev
 */
public class ConnectionNodeBuilderFactory {

    public synchronized static ConnectionNodeBuilder getBuilder() {
        Preferences preferences = Activator.getDefault().getPluginPreferences();

        NodeOrderStrategyAbstract strategy;

        boolean groupByType = preferences
                .getBoolean(PreferencesInitializer.GROUP_BY_TYPE_FIRST);
        boolean groupCanonically = preferences
                .getBoolean(PreferencesInitializer.GROUP_CANONICALLY);
        boolean groupByNaturalOrder = preferences
                .getBoolean(PreferencesInitializer.GROUP_BY_CREATIONAL_ORDER);

        if (groupCanonically) {
            strategy = new NodeOrderByCanonicalRepresentationStrategy();
        } else if (groupByNaturalOrder) {
            strategy = new NodeOrderByCreationalOrderStrategy();
        } else if (groupByType) {
            strategy = new NodeOrderByTypeStrategy();
        } else {
            // leave it like JConsole (default)
            strategy = new NodeOrderByTypeStrategy();
        }
        ConnectionNodeBuilder builder = new ConnectionNodeBuilder(strategy);
        return builder;
    }
}
