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

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.tree.InitializableNodeAbstract;
import org.openehealth.ipf.platform.manager.connection.ui.tree.Node;
import org.openehealth.ipf.platform.manager.jmxexplorer.IJMXExplorerMediator;
import org.openehealth.ipf.platform.manager.jmxexplorer.IMBeanServerConnectionFacade;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanAttributeNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanAttributesGroupNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNotificationNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNotificationsGroupNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanOperationNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanOperationsGroupNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.builder.strategy.NodeOrderStrategyAbstract;

/**
 * Uses different Strategies to determine the node order depth of the MBean
 * properties. Created by the {@link ConnectionNodeBuilderFactory}
 * 
 * @author Mitko Kolev
 */
public class ConnectionNodeBuilder {

    private final NodeOrderStrategyAbstract strategy;
    private static final Log log = LogFactory
            .getLog(ConnectionNodeBuilder.class);

    public ConnectionNodeBuilder(NodeOrderStrategyAbstract strategy) {
        this.strategy = strategy;
    }

    public void createConnectionNode(InitializableNodeAbstract connectionNode)
            throws IOException {
        IJMXExplorerMediator mediator = Activator.getDefault()
                .getJMXExplorerMediator();

        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) connectionNode
                .getAdapter(IConnectionConfiguration.class);

        IMBeanServerConnectionFacade mBeanServerConnectionFacade = mediator
                .getMBeanServerConnectionConfigurationFacade(connectionConfiguration);

        Set<ObjectName> mBeanNames = mBeanServerConnectionFacade
                .getObjectNames();
        Iterator<ObjectName> iter = mBeanNames.iterator();

        while (iter.hasNext()) {
            ObjectName objectName = iter.next();
            // sort all mbeans by domain
            factorizeByDomainName(connectionNode, objectName);
        }
        Node[] domainNodes = connectionNode.getChildren();
        for (int t = 0; t < domainNodes.length; t++) {
            Node domainNode = domainNodes[t];
            List<ObjectName> objectNames = domainNode.getObjectNames();
            for (ObjectName objectName : objectNames) {
                factorizeByPropertiesToParent(connectionConfiguration,
                        domainNode, objectName, 0, mBeanServerConnectionFacade);
            }
        }
    }

    private void factorizeByPropertiesToParent(
            IConnectionConfiguration connectionConfiguration, Node parentNode,
            ObjectName objectName, int factorDepth,
            IMBeanServerConnectionFacade mBeanServerConnectionFacade)
            throws IOException {

        String propertiesString = objectName.getKeyPropertyListString();

        Hashtable<String, String> properties = objectName.getKeyPropertyList();
        List<String> propertyNames = strategy.parseProperties(propertiesString);

        if (factorDepth == propertyNames.size()) {
            // replace the general node with an MBean Node
            Node parentGroup = parentNode.getParent();
            MBeanNode mbeanNode;
            MBeanInfo mbeanInfo;
            try {
                mbeanInfo = mBeanServerConnectionFacade
                        .getMBeanInfo(objectName);
                mbeanNode = new MBeanNode(parentNode.getName(), objectName,
                        mbeanInfo);

            } catch (InstanceNotFoundException infe) {
                // add an empty node!
                log.error(infe);
                mbeanNode = new MBeanNode(parentNode.getName(), objectName,
                        new EmptyMBeanInfo(parentNode.getName(), infe
                                .getLocalizedMessage()));
            } catch (IntrospectionException ie) {
                log.error(ie);
                // add an empty node!
                mbeanNode = new MBeanNode(parentNode.getName(), objectName,
                        new EmptyMBeanInfo(parentNode.getName(), ie
                                .getLocalizedMessage()));
            }

            // this must be the last child, so ordering is not an issue
            parentGroup.removeChild(parentNode);
            parentGroup.addChild(mbeanNode);
            // populate the attributes, notifications and operations
            updateMBeanNode(mbeanNode);

            return;
        }
        String currentPropertyName = propertyNames.get(factorDepth);
        String currentPropertyValue = properties.get(currentPropertyName);

        Node node = parentNode.getChildByName(currentPropertyValue);
        // if it does not exist, add it
        if (node == null) {
            node = new Node(currentPropertyValue);
            node.addObjectName(objectName);
            parentNode.addChild(node);
        } else {
            // add the current objectName to the objectNames which are in this
            // node.
            node.addObjectName(objectName);
        }
        factorizeByPropertiesToParent(connectionConfiguration, node,
                objectName, factorDepth + 1, mBeanServerConnectionFacade);
    }

    public void updateMBeanNode(MBeanNode mbeanNode) throws IOException {
        // create MBean's operations etc.
        Node[] children = mbeanNode.getChildren();
        // cleanup
        for (int t = 0; t < children.length; t++) {
            Node child = children[t];
            mbeanNode.removeChild(child);
        }

        // add all
        createAttributes(mbeanNode);
        createOperations(mbeanNode);
        createNotifications(mbeanNode);

    }

    /**
     * Creates one node for every the different domain.
     * 
     * @param parentNode
     * @param objectName
     */
    private void factorizeByDomainName(Node parentNode, ObjectName objectName) {
        // if the factor exists?
        String domainName = objectName.getDomain();
        Node node = parentNode.getChildByName(domainName);

        // if it does not exist, add it
        if (node == null) {
            node = new Node(domainName);
            node.addObjectName(objectName);
            parentNode.addChild(node);
        } else {
            // add the current objectName to the objectNames which are in this
            // node.
            node.addObjectName(objectName);
        }
    }

    private void createAttributes(MBeanNode mbeanNode) throws IOException {
        MBeanInfo mbeanInfo = mbeanNode.getMbeanInfo();
        MBeanAttributeInfo[] attributes = mbeanInfo.getAttributes();
        if (attributes == null || attributes.length == 0) {
            return;
        }
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) mbeanNode
                .getAdapter(IConnectionConfiguration.class);

        MBeanAttributesGroupNode attributesGroup = new MBeanAttributesGroupNode(
                connectionConfiguration, mbeanNode.getObjectName(), mbeanInfo);
        mbeanNode.addChild(attributesGroup);
        Arrays.sort(attributes, 0, attributes.length,
                new Comparator<MBeanAttributeInfo>() {

                    @Override
                    public int compare(MBeanAttributeInfo o1,
                            MBeanAttributeInfo o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

        for (int t = 0; t < attributes.length; t++) {
            MBeanAttributeInfo attribute = attributes[t];
            attributesGroup.addChild(new MBeanAttributeNode(
                    attribute.getName(), mbeanNode.getObjectName(), mbeanInfo,
                    attribute, connectionConfiguration));

        }

    }

    private void createOperations(MBeanNode mbeanNode) throws IOException {
        MBeanInfo mbeanInfo = mbeanNode.getMbeanInfo();
        MBeanOperationInfo[] operations = mbeanInfo.getOperations();
        if (operations == null || operations.length == 0) {
            return;
        }
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) mbeanNode
                .getAdapter(IConnectionConfiguration.class);
        Arrays.sort(operations, 0, operations.length,
                new Comparator<MBeanOperationInfo>() {

                    @Override
                    public int compare(MBeanOperationInfo o1,
                            MBeanOperationInfo o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

        MBeanOperationsGroupNode operationsGroup = new MBeanOperationsGroupNode(
                mbeanNode.getObjectName(), mbeanInfo);
        mbeanNode.addChild(operationsGroup);
        for (int t = 0; t < operations.length; t++) {
            MBeanOperationInfo operation = operations[t];
            operationsGroup.addChild(new MBeanOperationNode(
                    operation.getName(), mbeanNode.getObjectName(), mbeanInfo,
                    operation, connectionConfiguration));
        }
    }

    private void createNotifications(MBeanNode mbeanNode) throws IOException {
        MBeanInfo mbeanInfo = mbeanNode.getMbeanInfo();
        MBeanNotificationInfo[] notifications = mbeanInfo.getNotifications();
        if (notifications == null || notifications.length == 0) {
            return;
        }
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) mbeanNode
                .getAdapter(IConnectionConfiguration.class);

        MBeanNotificationsGroupNode notificationsNode = new MBeanNotificationsGroupNode();
        mbeanNode.addChild(notificationsNode);

        for (int t = 0; t < notifications.length; t++) {
            MBeanNotificationInfo notification = notifications[t];
            notificationsNode.addChild(new MBeanNotificationNode(notification
                    .getName(), mbeanNode.getObjectName(), mbeanInfo,
                    notification, connectionConfiguration));
        }
    }
}
