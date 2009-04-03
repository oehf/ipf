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
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;

/**
 * A general purpose TableColumnViewer sorter, which sorts the elements with a
 * sorter given by the client.
 * 
 * @author Mitko Kolev
 */
public abstract class TableColumnViewerSorter extends ColumnViewerSorter {

    private final TableViewerColumn tableViewerColumn;

    public TableColumnViewerSorter(ColumnViewer viewer, TableViewerColumn column) {
        super(viewer);
        this.tableViewerColumn = column;
        this.tableViewerColumn.getColumn().addSelectionListener(
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
        Table table = tableViewerColumn.getColumn().getParent();

        if (direction == NONE) {
            table.setSortColumn(null);
            table.setSortDirection(SWT.NONE);
        } else {
            table.setSortColumn(tableViewerColumn.getColumn());
            if (direction == DOWN) {
                table.setSortDirection(SWT.DOWN);
            } else {// Direction = UP
                table.setSortDirection(SWT.UP);
            }
            updateViewer(this);
        }

    }

}