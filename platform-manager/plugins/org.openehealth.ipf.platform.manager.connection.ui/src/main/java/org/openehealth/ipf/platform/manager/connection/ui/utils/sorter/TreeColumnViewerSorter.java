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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Sorter for a Tree column.
 * 
 * @author Mitko Kolev
 */
public abstract class TreeColumnViewerSorter extends ViewerComparator {

    private static final int ASC = 1;

    private static final int NONE = 0;

    private static final int DESC = -1;

    private int direction = 0;

    private final TreeViewerColumn column;

    private final ColumnViewer viewer;

    public TreeColumnViewerSorter(ColumnViewer viewer, TreeViewerColumn column) {
        this.column = column;
        this.viewer = viewer;
        this.column.getColumn().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (TreeColumnViewerSorter.this.viewer.getComparator() != null) {
                    if (TreeColumnViewerSorter.this.viewer.getComparator() == TreeColumnViewerSorter.this) {
                        int tdirection = TreeColumnViewerSorter.this.direction;

                        if (tdirection == ASC) {
                            setSorter(TreeColumnViewerSorter.this, DESC);
                        } else if (tdirection == DESC) {
                            setSorter(TreeColumnViewerSorter.this, NONE);
                        }
                    } else {
                        setSorter(TreeColumnViewerSorter.this, ASC);
                    }
                } else {
                    setSorter(TreeColumnViewerSorter.this, ASC);
                }
            }
        });
    }

    public void setSorter(TreeColumnViewerSorter sorter, int direction) {
        if (direction == NONE) {
            column.getColumn().getParent().setSortColumn(null);
            column.getColumn().getParent().setSortDirection(SWT.NONE);
            viewer.setComparator(null);
        } else {
            column.getColumn().getParent().setSortColumn(column.getColumn());
            sorter.direction = direction;

            if (direction == ASC) {
                column.getColumn().getParent().setSortDirection(SWT.DOWN);
            } else {
                column.getColumn().getParent().setSortDirection(SWT.UP);
            }

            if (viewer.getComparator() == sorter) {
                viewer.refresh();
            } else {
                viewer.setComparator(sorter);
            }

        }
    }

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        return direction * doCompare(viewer, e1, e2);
    }

    protected abstract int doCompare(Viewer viewer, Object e1, Object e2);
}
