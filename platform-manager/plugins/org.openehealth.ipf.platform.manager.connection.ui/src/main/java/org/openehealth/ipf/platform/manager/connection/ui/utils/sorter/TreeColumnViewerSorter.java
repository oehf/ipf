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
package org.openehealth.ipf.platform.manager.connection.ui.utils.sorter;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;

/**
 * A general purpose TreeColumnViewer sorter, which sorts the elements with a
 * sorter given by the client.
 * 
 * @author Mitko Kolev
 */
public abstract class TreeColumnViewerSorter extends ColumnViewerSorter {

    private final TreeViewerColumn treeViewerColumn;

    public TreeColumnViewerSorter(ColumnViewer viewer, TreeViewerColumn column) {
        super(viewer);
        this.treeViewerColumn = column;
        // add a selection listener to the Tree column
        this.treeViewerColumn.getColumn().addSelectionListener(
                new SelectionDirectionModifier());
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.openehealth.ipf.platform.manager.connection.ui.utils.sorter.
     * ColumnViewerSorter
     * #setSorterDirection(org.openehealth.ipf.platform.manager
     * .connection.ui.utils.sorter.ColumnViewerSorter.Direction)
     */
    @Override
    public void setSorterDirection(Direction direction) {

        super.setSorterDirection(direction);
        Tree tree = treeViewerColumn.getColumn().getParent();

        if (direction == NONE) {// Do not sort
            tree.setSortColumn(null);
            tree.setSortDirection(SWT.NONE);
        } else {
            tree.setSortColumn(treeViewerColumn.getColumn());
            if (direction == DOWN) {
                tree.setSortDirection(SWT.DOWN);
            } else {// Direction = UP
                tree.setSortDirection(SWT.UP);
            }
            updateViewer(this);
        }

    }

}
