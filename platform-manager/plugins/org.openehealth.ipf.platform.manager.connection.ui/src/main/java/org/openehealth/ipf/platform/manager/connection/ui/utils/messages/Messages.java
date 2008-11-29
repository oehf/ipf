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
package org.openehealth.ipf.platform.manager.connection.ui.utils.messages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Messages appropriate for this plug-in
 * 
 * @author Mitko Kolev
 */
public class Messages {

    private final static Log log = LogFactory.getLog(Messages.class);

    private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(Messages.class.getName());

    private Messages() {
    }

    public static String getLabelString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            log.error("Resource not found: ", e);
            return '!' + key + '!';
        }
    }

}