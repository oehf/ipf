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
package org.openehealth.ipf.platform.manager.connection.ui.view;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.connection.ui.osgi.Activator;

/**
 * 
 * Provides label for a connection in the UI.
 * 
 * @author Mitko Kolev
 */
public class JMXConnectionLabelProvider extends ColumnLabelProvider implements
        ITableLabelProvider {

    private Image connectionConnectedImage;

    private ImageDescriptor connectionConnectedImageDescriptor;

    private Image connectionDisconnectedImage;

    private ImageDescriptor connectionDisconnectedImageDescriptor;

    private final int column;

    public JMXConnectionLabelProvider(int column) {
        super();
        this.column = column;
        Display display = Display.getCurrent();
        if (display != null && !display.isDisposed()) {

            connectionConnectedImage = null;
            connectionConnectedImageDescriptor = null;
            connectionDisconnectedImage = null;
            connectionDisconnectedImageDescriptor = null;

            connectionConnectedImageDescriptor = Activator.getDefault()
                    .getImageDescriptor("icons/connection/bullet_green.png");
            if (connectionConnectedImageDescriptor != null) {
                connectionConnectedImage = connectionConnectedImageDescriptor
                        .createImage();
            }

            connectionDisconnectedImageDescriptor = Activator.getDefault()
                    .getImageDescriptor("icons/connection/bullet_yellow.png");
            if (connectionDisconnectedImageDescriptor != null) {
                connectionDisconnectedImage = connectionDisconnectedImageDescriptor
                        .createImage();
            }

        }

    }

    public String getColumnText(Object element, int index) {
        IAdaptable adapter = (IAdaptable) element;
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) adapter
                .getAdapter(IConnectionConfiguration.class);
        switch (index) {
        case 0:
            return connectionConfiguration.getName();
        case 1:
            return connectionConfiguration.getHost();
        case 2:
            return String.valueOf(connectionConfiguration.getPort());
        default:
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang
     * .Object, int)
     */
    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        if (columnIndex > 0) {
            // set image only for the first column
            return null;
        }
        IJMXConnectionManager jMXConnectionManager = Activator.getDefault()
                .getJMXConnectionManager();

        IAdaptable adapter = (IAdaptable) element;
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) adapter
                .getAdapter(IConnectionConfiguration.class);
        if (jMXConnectionManager.isConnected(connectionConfiguration)) {
            return connectionConnectedImage;
        } else {
            return connectionDisconnectedImage;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (connectionConnectedImageDescriptor != null) {
            if (connectionConnectedImage != null)
                connectionConnectedImageDescriptor
                        .destroyResource(this.connectionConnectedImage);
        }
        if (connectionDisconnectedImageDescriptor != null) {
            if (connectionDisconnectedImage != null)
                connectionDisconnectedImageDescriptor
                        .destroyResource(this.connectionDisconnectedImage);
        }
    }

    @Override
    public String getText(Object element) {
        return getColumnText(element, this.column);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
     */
    @Override
    public Image getImage(Object element) {
        return getColumnImage(element, this.column);
    }

}
