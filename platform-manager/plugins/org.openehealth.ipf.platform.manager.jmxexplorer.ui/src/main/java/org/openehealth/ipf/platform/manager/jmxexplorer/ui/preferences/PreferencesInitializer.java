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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi.Activator;

/**
 * Preferences initalizer for the JMX preferences
 * 
 * @see JMXExplorerPreferencePage
 * 
 * @author Mitko Kolev
 */
public class PreferencesInitializer extends AbstractPreferenceInitializer {

    public static final String GROUP_BY_TYPE_FIRST = "group.by.type";

    public static final boolean GROUP_BY_TYPE_FIRST_DEFAULT_VALUE = true;

    public static final String GROUP_CANONICALLY = "group.canonically";

    public static final boolean GROUP_CANONICALLY_DEFAULT_VALUE = false;

    public static final String GROUP_BY_CREATIONAL_ORDER = "group.by.creational.order";

    public static final boolean GROUP_BY_CREATIONAL_ORDER_DEFAULT_VALUE = false;

    private final ScopedPreferenceStore preferences;

    /**
     * Initializes the preferences from the Scoped preferences for this plugin
     * (Activator.PLUGIN_ID).
     */
    public PreferencesInitializer() {
        preferences = new ScopedPreferenceStore(new ConfigurationScope(),
                Activator.PLUGIN_ID);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        preferences.setDefault(GROUP_BY_TYPE_FIRST,
                GROUP_BY_TYPE_FIRST_DEFAULT_VALUE);
        preferences.setDefault(GROUP_CANONICALLY,
                GROUP_CANONICALLY_DEFAULT_VALUE);
        preferences.setDefault(GROUP_BY_CREATIONAL_ORDER,
                GROUP_BY_CREATIONAL_ORDER_DEFAULT_VALUE);

    }

}
