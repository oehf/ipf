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
package org.openehealth.ipf.platform.manager.flowmanager.ui.view;

import java.text.SimpleDateFormat;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.ui.adapter.FlowInfoAdapter;
import org.openehealth.ipf.platform.manager.flowmanager.ui.osgi.Activator;

/**
 * Labels the FlowInfo object in a table.
 * 
 * @see org.eclipse.jface.viewers.ITableLabelProvider
 * 
 * @author Mitko Kolev
 */
public class FlowInfoLabelProvider extends ColumnLabelProvider implements
        ITableLabelProvider {

    private final ImageDescriptor failedImageDescriptor;

    private final Image failedImage;

    private final ImageDescriptor acknowledgedImageDescriptor;

    private final Image acknowledgedImage;

    private final int row;

    public FlowInfoLabelProvider(int row) {
        this.row = row;
        if (row == 0) {
            Display display = Display.getCurrent();
            if (display == null || display.isDisposed()) {
                failedImageDescriptor = null;
                failedImage = null;

                acknowledgedImageDescriptor = null;
                acknowledgedImage = null;

            } else {
                acknowledgedImageDescriptor = Activator
                        .getImageDescriptor("icons/bullet_green.png");
                acknowledgedImage = acknowledgedImageDescriptor.createImage();

                failedImageDescriptor = Activator
                        .getImageDescriptor("icons/bullet_red.png");
                failedImage = failedImageDescriptor.createImage();
            }
        } else {
            failedImageDescriptor = null;
            failedImage = null;
            acknowledgedImageDescriptor = null;
            acknowledgedImage = null;
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
        FlowInfoAdapter adapter = (FlowInfoAdapter) element;
        IFlowInfo flow = adapter.getFlowInfo();
        if (columnIndex == 0) {
            if (flow.getStatus() != null && flow.getStatus().equals("ERROR")) {
                return failedImage;
            }
            return acknowledgedImage;
        }
        return null;
    }

    @Override
    public String getText(Object element) {
        return getColumnText(element, this.row);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
     */
    @Override
    public Image getImage(Object element) {
        return getColumnImage(element, this.row);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang
     * .Object, int)
     */
    @Override
    public String getColumnText(Object element, int columnIndex) {
        FlowInfoAdapter adapter = (FlowInfoAdapter) element;
        IFlowInfo flow = adapter.getFlowInfo();

        switch (columnIndex) {
        case 0:
            return String.valueOf(flow.getIdentifier());
        case 1:
            return flow.getStatus() == null ? "" : flow.getStatus();

        case 2:
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy.MM.dd HH:mm:ss");
            String time = format.format(flow.getCreationTime());
            return time;
        case 3:
            return flow.getApplication() == null ? "" : flow.getApplication();
        case 4:
            IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) adapter
                    .getAdapter(IConnectionConfiguration.class);
            if (connectionConfiguration == null) {
                return "";
            }
            return connectionConfiguration.getName();
        case 5:
            return String.valueOf(flow.getAckCount());
        case 6:
            return String.valueOf(flow.getAckCountExpected());
        default:
            return "";
        }
    }

    @Override
    public void dispose() {
        if (failedImageDescriptor != null) {
            failedImageDescriptor.destroyResource(failedImage);
        }
        if (acknowledgedImageDescriptor != null) {
            acknowledgedImageDescriptor.destroyResource(acknowledgedImage);
        }

    }

}
