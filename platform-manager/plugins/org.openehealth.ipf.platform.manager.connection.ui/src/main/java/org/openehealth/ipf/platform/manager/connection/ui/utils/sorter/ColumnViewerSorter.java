/*
 * Copyright 2009 the original author or authors.
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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Base class which supports sorting of JFace ColumnViewer components.
 * 
 * @author Mitko Kolev
 */
public abstract class ColumnViewerSorter extends ViewerComparator {

    final static Direction DOWN = new Direction(1);

    final static Direction NONE = new Direction(0);

    final static Direction UP = new Direction(-1);

    /**
     * The initial direction.
     */
    private Direction direction = NONE;

    public Direction getDirection() {
        return direction;
    }

    private final ColumnViewer viewer;

    public ColumnViewer getViewer() {
        return viewer;
    }

    public ColumnViewerSorter(ColumnViewer viewer) {
        this.viewer = viewer;
    }

    /**
     * Must be added manually (no suitable interfaces provided by JFace) Changes
     * the selection.
     */
    class SelectionDirectionModifier extends SelectionAdapter {

        @Override
        public void widgetSelected(SelectionEvent e) {
            if (viewer.getComparator() != null) {
                if (viewer.getComparator() == ColumnViewerSorter.this) {
                    Direction currentDirection = direction;

                    if (currentDirection == DOWN) {
                        setSorterDirection(UP);
                    } else if (currentDirection == UP) {
                        setSorterDirection(NONE);
                    }
                } else {
                    setSorterDirection(DOWN);
                }
            } else {
                setSorterDirection(DOWN);
            }
        }
    }

    void updateViewer(ColumnViewerSorter sorter) {
        if (viewer.getComparator() == sorter) {
            viewer.refresh();
        } else {
            viewer.setComparator(sorter);
        }
    }

    /**
     * Sets the sorter direction to the given direction. Updates the viewer if
     * the direction is {@link #NONE}, otherwise sets the direction to the given
     * direction. Called on a click.
     * 
     * @param direction
     *            the desired direction, supporeted is {@link #NONE},
     *            {@link #UP} and {@link #DOWN}
     */
    void setSorterDirection(Direction direction) {
        if (direction == NONE) {
            viewer.setComparator(null);
        } else {
            this.direction = direction;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.
     * viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        return direction.getIntValue() * doCompare(viewer, e1, e2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.
     * viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    protected abstract int doCompare(Viewer viewer, Object e1, Object e2);

    final static class Direction {
        private final int intValue;

        private Direction(int intValue) {
            this.intValue = intValue;
        }

        public int getIntValue() {
            return intValue;
        }
    }
}
