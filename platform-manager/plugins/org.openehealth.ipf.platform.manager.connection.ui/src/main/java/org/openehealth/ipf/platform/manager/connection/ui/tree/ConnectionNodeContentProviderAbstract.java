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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;

/**
 * Content provider for a JMX Beans of a Connection.
 * 
 * @author Mitko Kolev
 */
public abstract class ConnectionNodeContentProviderAbstract implements
        IStructuredContentProvider, ITreeContentProvider {

    private final Log log = LogFactory
            .getLog(ConnectionNodeContentProviderAbstract.class);

    private final IViewSite owner;

    public ConnectionNodeContentProviderAbstract(IViewSite owner) {
        this.owner = owner;
    }

    /**
     * Calls the initializeTree() method.
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
     *      java.lang.Object, java.lang.Object)
     */
    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        try {
            initialize();
        } catch (IOException e) {
            log.error("Exception on tree initialization", e);
        }
    }

    /**
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
    }

    /**
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object parent) {
        if (parent.equals(owner)) {
            if (getTree() == null) {
                try {
                    initialize();
                } catch (IOException ioe) {
                    log.error(ioe);
                }
            }
            return getChildren(getTree());
        }
        return getChildren(parent);
    }

    /**
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object child) {
        if (child instanceof Node) {
            return ((Node) child).getParent();
        }
        return null;
    }

    /**
     * s
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parent) {
        if (parent instanceof Node) {
            return ((Node) parent).getChildren();
        }
        return new Object[0];
    }

    /**
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object parent) {
        if (parent instanceof Node)
            return ((Node) parent).hasChildren();
        return false;
    }

    /**
     * Returns the viewsite of this content provider. the element must be of
     * type IViewSite according to the eclipse usability guidelines.
     * 
     * @return
     */
    public IViewSite getOwner() {
        return owner;
    }

    /**
     * Returns the tree element (subelement of the View). this element is
     * considered the tree parent of the view.
     * 
     * @return the tree.
     */
    public abstract Node getTree();

    /**
     * Performs tree initialization where all sub-nodes of the tree of type
     * initializableNode are being initialized.
     * 
     * @throws IOException
     *             from the initialize method of the child
     */
    public abstract void initialize() throws IOException;
}
