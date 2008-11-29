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
package org.openehealth.ipf.platform.manager.flowmanager.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.openehealth.ipf.platform.manager.flowmanager.ui.osgi.Activator;

/**
 * Initializes the FlowManager preferences.
 * 
 * @author Mitko Kolev
 */
public class FlowManagerPreferencesInitializer extends AbstractPreferenceInitializer {

    private final ScopedPreferenceStore preferences;

    public FlowManagerPreferencesInitializer() {
        preferences = new ScopedPreferenceStore(new ConfigurationScope(),
                Activator.PLUGIN_ID);
    }

    @Override
    public void initializeDefaultPreferences() {
        preferences.setDefault(FlowManagerPreferencesConstants.REFRESH_DELAY_KEY,
                FlowManagerPreferencesConstants.REFRESH_DELAY_DEFAULT_VALUE);
    }
}
