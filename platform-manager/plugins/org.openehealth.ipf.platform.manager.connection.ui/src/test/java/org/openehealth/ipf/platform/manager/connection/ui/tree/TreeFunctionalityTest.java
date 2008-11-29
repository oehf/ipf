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
package org.openehealth.ipf.platform.manager.connection.ui.tree;

import java.io.IOException;

import junit.framework.TestCase;

import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.mock.JMXConnectionManagerImplMock;

/**
 * 
 * @author Mitko Kolev
 */
public class TreeFunctionalityTest extends TestCase {

    Node simpleTree;

    InitializableNodeAbstract connectionNode;

    IConnectionConfiguration connectionConfiguration;

    @Override
    public void setUp() throws IOException {
        this.simpleTree = new Node("Root");
        simpleTree.addChild(new Node("Root/Child1"));
        simpleTree.addChild(new Node("Root/Child2"));
        JMXConnectionManagerImplMock connectionManager = new JMXConnectionManagerImplMock();
        connectionConfiguration = connectionManager
                .getConnectionConfigurations().get(0);

        connectionNode = new InitializableNodeAbstract(connectionConfiguration,
                "connection1") {

            @Override
            public void initialize() throws IOException {
                this.addChild(simpleTree);
            }
        };

    }

    public void testTree() {
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) simpleTree
                .getAdapter(IConnectionConfiguration.class);
        assertTrue(connectionConfiguration == null);
        // test get first node
        Node node = simpleTree.getChildByName("Root/Child1");
        assertTrue(node != null);
        assertTrue(node.getName().equals("Root/Child1"));
        // test get second node
        node = simpleTree.getChildByName("Root/Child2");
        assertTrue(node != null);
        assertTrue(node.getName().equals("Root/Child2"));

        Node children[] = simpleTree.getChildren();
        for (int t = 0; t < children.length; t++) {
            Node child = children[t];
            assertTrue(child.getParent().equals(simpleTree));
            assertTrue(child.getChildren().length == 0);
            assertTrue(child.isLeaf() == true);
            assertTrue(child.getObjectNames().size() == 0);
            assertTrue(child.hasChildren() == false);
            assertTrue(child.getName().equals(
                    new String("Root/Child" + (t + 1))));
        }

    }

    public void testConnectionNodeFunctionality() throws IOException {
        connectionNode.initialize();
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) connectionNode
                .getAdapter(IConnectionConfiguration.class);
        assertTrue(connectionConfiguration.equals(this.connectionConfiguration));
        Node node = connectionNode.getChildByName("Root");
        assertTrue(node.equals(simpleTree));
        connectionNode.removeChild(node);
        assertTrue(node.getParent() == null);
        assertTrue(connectionNode.getChildren().length == 0);
        assertTrue(connectionNode.hasChildren() == false);
        assertTrue(connectionNode.getObjectNames().size() == 0);

    }
}
