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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.ObjectName;

import org.eclipse.core.runtime.IAdaptable;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * Class Representing a node with associated ObjectNames.
 * 
 * @author Mitko Kolev
 */
public class Node implements IAdaptable {

    private final String name;

    private Node parent;

    private final ArrayList<Node> children;

    private final List<ObjectName> objectNames;

    /**
     * Construcs a node with a given name.
     * 
     * @param name
     */
    public Node(String name) {
        this.name = name;
        children = new ArrayList<Node>();
        objectNames = new ArrayList<ObjectName>();
    }

    /**
     * Adds an ObjectName to the objectNames associated with this Node
     * 
     * @param objectName
     */
    public void addObjectName(ObjectName objectName) {
        if (!objectNames.contains(objectName)) {
            objectNames.add(objectName);
        }
    }

    /**
     * Returns the list of ObjectNames associated with this node.
     * 
     * @return an unmodifiableList
     */
    public List<ObjectName> getObjectNames() {
        return Collections.unmodifiableList(objectNames);
    }

    /**
     * Returns the name of this node.
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the parent of this node.
     * 
     * @param parent
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    /**
     * Returns the name of this node.
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {

        if (adapter == IConnectionConfiguration.class) {
            if (parent != null)
                return parent.getAdapter(adapter);
            else
                return null;
        }
        return null;
    }

    /**
     * Adds a child to this node's children.
     * 
     * @param child
     */
    public void addChild(Node child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * Removes the given node from this node's children.
     * 
     * @param child
     */
    public void removeChild(Node child) {
        if (children.contains(child)) {
            children.remove(child);
            child.setParent(null);
        }
    }

    /**
     * Returns this node's children.
     * 
     * @return
     */
    public Node[] getChildren() {
        return children.toArray(new Node[children.size()]);
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public boolean isLeaf() {
        return !hasChildren();
    }

    /**
     * Returns the direct child of this node with the given name.
     * 
     * @param name
     *            the name of the child
     * @return
     */
    public Node getChildByName(String name) {
        if (name == null)
            return null;
        for (Node node : children) {
            if (node.getName() != null && node.getName().equals(name)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Returns if this node matches the given pattern.
     * 
     * @param pattern
     * @return
     */
    public boolean matches(String pattern) {
        try {
            return this.getName().matches(pattern);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns if this node or its children match the given pattern.
     * 
     * @param pattern
     * @return
     */
    public boolean matchesDeep(String pattern) {
        if (name == null)
            return false;
        if (matches(pattern))
            return true;
        for (Node node : children) {
            if (node.getName() == null) {
                return false;
            } else {
                return node.matchesDeep(pattern);
            }
        }
        return false;
    }

    /**
     * Returns if this node has a direct child with this name.
     * 
     * @param name
     * @return
     */
    public boolean hasChildWithName(String name) {
        if (name == null)
            return false;
        for (Node node : children) {
            if (node.getName() != null && node.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((objectNames == null) ? 0 : objectNames.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Node other = (Node) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (objectNames == null) {
            if (other.objectNames != null)
                return false;
        } else if (!objectNames.equals(other.objectNames))
            return false;
        return true;
    }
}
