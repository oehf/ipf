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
package org.openehealth.ipf.platform.manager.connection.ui.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * 
 * Helper class for Connection UI routines.
 * 
 * @author Mitko Kolev
 */
public class ConnectionUtils {
    /**
     * Extracts a single connection object from the selection. If no connection
     * is selected, return null.
     * 
     * @param selection
     * @return null if no connection is selected.
     */
    public static IConnectionConfiguration getSingleConnectionConfigurationObject(
            ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection is = (IStructuredSelection) selection;
            if (is.isEmpty()) {
                return null;
            }
            Object element = is.getFirstElement();
            if (element instanceof IAdaptable) {
                IAdaptable adapter = (IAdaptable) element;
                return (IConnectionConfiguration) adapter
                        .getAdapter(IConnectionConfiguration.class);

            }
        }
        return null;
    }

    /**
     * Extracts a list of connection objects from the selection. If there are no
     * connections in the selection, returns an empty list.
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<IConnectionConfiguration> getConnectionConfigurations(
            ISelection selection) {
        List<IConnectionConfiguration> connectionConfigurations = new ArrayList<IConnectionConfiguration>();
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection is = (IStructuredSelection) selection;
            if (is.isEmpty()) {
                return connectionConfigurations;
            }
            Iterator connectionsIterator = is.iterator();
            while (connectionsIterator.hasNext()) {
                IAdaptable adaptable = (IAdaptable) connectionsIterator.next();
                Object adapter = adaptable
                        .getAdapter(IConnectionConfiguration.class);
                if (adapter instanceof IConnectionConfiguration) {
                    connectionConfigurations
                            .add((IConnectionConfiguration) adapter);
                }
            }
        }
        return connectionConfigurations;
    }

}
