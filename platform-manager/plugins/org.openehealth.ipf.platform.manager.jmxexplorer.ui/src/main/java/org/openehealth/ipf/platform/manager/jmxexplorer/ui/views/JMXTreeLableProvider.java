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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.views;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.JMXConnectionNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanAttributeNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanAttributesGroupNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNotificationNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNotificationsGroupNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanOperationNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanOperationsGroupNode;

/**
 * Label provider for the JMX Tree
 * TODO:// should be refactored to extend the JMX Connection LabelProvider
 * 
 * @see LabelProvider
 * @see JMXView
 * @author Mitko Kolev
 */
public class JMXTreeLableProvider extends LabelProvider {

    private final static String IMG_CONNECTION_CONNECTED = "icons/bullet_green.png";
    
    private final static String IMG_CONNECTION_DISCONNECTED = "icons/bullet_yellow.png"; 

    private final static String IMG_ATTRIBUTES_GROUP_KEY = "icons/bullet_yellow.png";

    private final static String IMG_OPERATIONS_GROUP_KEY = "icons/bullet_orange.png";

    private final static String IMG_NOTIFICATIONS_GROUP_KEY = "icons/bullet_purple.png";

    private final static String IMG_MBEAN_KEY = "icons/database_edit.png";

    private final static String IMG_ATTRIBUTES_DISABLED_KEY = "icons/bullet_black.png";

    private ImageDescriptor connectionDisconnectedImageDescriptor;

    private Image connectionDisconnectedImage;
    
    private ImageDescriptor connectionConnectedImageDescriptor;

    private Image connectionConnectedImage;

    private ImageDescriptor attributesGroupImageDescriptor;

    private Image attributesGroupImage;

    private ImageDescriptor operationsGroupImageDescriptor;

    private Image operationsGroupImage;

    private ImageDescriptor notificationsGroupImageDescriptor;

    private Image notificationsGroupImage;

    private ImageDescriptor mbeanImageDescriptor;

    private Image mbeanImage;

    private ImageDescriptor attributesGroupDisabledImageDescriptor;

    private Image attributesGroupDisabledImage;

    @Override
    public String getText(Object obj) {
        return obj.toString();
    }

    @Override
    public Image getImage(Object obj) {
        String imageKey = ISharedImages.IMG_OBJ_FOLDER;
        if (obj instanceof JMXConnectionNode) {
            imageKey = IMG_CONNECTION_CONNECTED;
            if (connectionConnectedImage == null) {
                connectionConnectedImageDescriptor = Activator.getDefault()
                        .getImageDescriptor(imageKey);
                connectionConnectedImage = connectionConnectedImageDescriptor
                        .createImage();
            }
            imageKey = IMG_CONNECTION_DISCONNECTED;
            if (connectionDisconnectedImage == null) {
                connectionDisconnectedImageDescriptor = Activator.getDefault()
                        .getImageDescriptor(imageKey);
                connectionDisconnectedImage = connectionDisconnectedImageDescriptor
                        .createImage();
            }
            if (((JMXConnectionNode) obj).hasChildren()) {
                return connectionConnectedImage;
            } else {
                //if the nodes are updated automatically, they will be updated always when 
                //the connection is online.
                return connectionDisconnectedImage;
            }

        } else if (obj instanceof MBeanAttributesGroupNode) {
            if (attributesGroupImage == null) {
                attributesGroupImageDescriptor = Activator.getDefault()
                        .getImageDescriptor(IMG_ATTRIBUTES_GROUP_KEY);
                attributesGroupImage = attributesGroupImageDescriptor
                        .createImage();
            }
            return attributesGroupImage;
        } else if (obj instanceof MBeanAttributeNode) {
            if (attributesGroupImage == null) {
                attributesGroupImageDescriptor = Activator.getDefault()
                        .getImageDescriptor(IMG_ATTRIBUTES_GROUP_KEY);
                attributesGroupImage = attributesGroupImageDescriptor
                        .createImage();
            }
            MBeanAttributeNode node = (MBeanAttributeNode) obj;
            // return icons only for the writable attributes
            if (node.isWritable()) {
                return attributesGroupImage;
            } else {
                if (attributesGroupDisabledImage == null) {
                    attributesGroupDisabledImageDescriptor = Activator
                            .getDefault().getImageDescriptor(
                                    IMG_ATTRIBUTES_DISABLED_KEY);
                    attributesGroupDisabledImage = attributesGroupDisabledImageDescriptor
                            .createImage();
                }
                return attributesGroupDisabledImage;
            }
        } else if (obj instanceof MBeanOperationsGroupNode
                || obj instanceof MBeanOperationNode) {
            if (operationsGroupImage == null) {
                operationsGroupImageDescriptor = Activator.getDefault()
                        .getImageDescriptor(IMG_OPERATIONS_GROUP_KEY);
                operationsGroupImage = operationsGroupImageDescriptor
                        .createImage();
            }
            return operationsGroupImage;
        } else if (obj instanceof MBeanNotificationsGroupNode
                || obj instanceof MBeanNotificationNode) {
            if (notificationsGroupImage == null) {
                notificationsGroupImageDescriptor = Activator.getDefault()
                        .getImageDescriptor(IMG_NOTIFICATIONS_GROUP_KEY);
                notificationsGroupImage = notificationsGroupImageDescriptor
                        .createImage();
            }
            return notificationsGroupImage;
        } else if (obj instanceof MBeanNode) {
            if (mbeanImage == null) {
                mbeanImageDescriptor = Activator.getDefault()
                        .getImageDescriptor(IMG_MBEAN_KEY);
                mbeanImage = mbeanImageDescriptor.createImage();
            }
            return mbeanImage;
        }
        return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
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
        if (notificationsGroupImageDescriptor != null) {
            if (notificationsGroupImage != null)
                notificationsGroupImageDescriptor
                        .destroyResource(this.notificationsGroupImage);
        }
        if (attributesGroupImageDescriptor != null) {
            if (attributesGroupImage != null)
                attributesGroupImageDescriptor
                        .destroyResource(this.attributesGroupImage);
        }
        if (operationsGroupImageDescriptor != null) {
            if (operationsGroupImage != null)
                operationsGroupImageDescriptor
                        .destroyResource(this.operationsGroupImage);
        }
        if (mbeanImageDescriptor != null) {
            if (mbeanImage != null)
                mbeanImageDescriptor.destroyResource(this.mbeanImage);
        }
        if (attributesGroupDisabledImageDescriptor != null) {
            if (attributesGroupDisabledImage != null)
                attributesGroupDisabledImageDescriptor
                        .destroyResource(this.attributesGroupDisabledImage);
        }

    }
}
