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

import java.io.IOException;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;
import org.openehealth.ipf.platform.manager.connection.ui.tree.ConnectionNodeContentProviderAbstract;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.JMXConnectionTree;

/**
 * The JMX view's content provider.
 * 
 * @see JMXView
 * @see ConnectionNodeContentProviderAbstract
 * 
 * @author Mitko Kolev
 */
public class JMXTreeContentProvider extends
        ConnectionNodeContentProviderAbstract {

    private JMXConnectionTree tree;

    public JMXTreeContentProvider(IViewSite owner) {
        super(owner);
    }

    @Override
    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    }

    @Override
    public void initialize() throws IOException {
        tree = new JMXConnectionTree();
    }

    /**
     * @see org.openehealth.ipf.platform.manager.connection.ui.tree.tree.ConnectionNodeContentProviderAbstract#getTree()
     */
    @Override
    public JMXConnectionTree getTree() {
        return tree;
    }

}
