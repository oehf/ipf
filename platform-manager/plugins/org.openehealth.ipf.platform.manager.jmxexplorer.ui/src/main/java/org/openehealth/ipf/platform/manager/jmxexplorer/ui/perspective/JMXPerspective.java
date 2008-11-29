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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

/**
 * Definition of the JMX perspective.
 * 
 * @see IPerspectiveFactory
 * @author Mitko Kolev
 */
public class JMXPerspective implements IPerspectiveFactory {

    private final String ID = JMXPerspective.class.getName();
    private final String MAIN_LEFT_ID = ID + ".main.left";
    private final String MAIN_LEFT_TOP_ID = MAIN_LEFT_ID + ".top";
    private final String MAIN_LEFT_BOTTOM_ID = MAIN_LEFT_ID + ".bottom";
    private final String BOTTOM_RESULTS_ID = ID + ".bottom.results";

    @Override
    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();

        // Put the Outline view on the left.

        layout.createPlaceholderFolder(MAIN_LEFT_ID, IPageLayout.LEFT, 0.3f,
                editorArea);
        layout.createPlaceholderFolder(MAIN_LEFT_TOP_ID, IPageLayout.TOP,
                0.67f, MAIN_LEFT_ID);
        IFolderLayout leftBottom = layout.createFolder(MAIN_LEFT_BOTTOM_ID,
                IPageLayout.BOTTOM, 0.4f, MAIN_LEFT_ID);
        leftBottom.addView(IPageLayout.ID_PROP_SHEET);

        IFolderLayout bottom = layout.createFolder(BOTTOM_RESULTS_ID,
                IPageLayout.BOTTOM, 0.6f, editorArea);
        bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
        layout.setFixed(false);

    }
}
