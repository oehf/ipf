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
package org.openehealth.ipf.platform.manager.connection.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.openehealth.ipf.platform.manager.connection.ui.wizards.NewConnectionWizard;

/**
 * The class defines place-holders for the connection relevant views.
 * 
 * @author Mitko Kolev
 */
public class DefaultPerspective implements IPerspectiveFactory {

    private final static String ID = DefaultPerspective.class.getName();

    private final static String MAIN_LEFT_ID = ID + ".main.left";

    private final static String MAIN_LEFT_TOP_ID = MAIN_LEFT_ID + ".top";

    private final static String MAIN_LEFT_BOTTOM_ID = MAIN_LEFT_ID + ".bottom";

    private final static String BOTTOM_RESULTS_ID = ID + ".bottom.results";

    public final static String NEW_CONNECTION_WIZARD_ID = NewConnectionWizard.class
            .getName();

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();

        // Put the Outline view on the left.

        layout.createPlaceholderFolder(MAIN_LEFT_ID, IPageLayout.LEFT, 0.35f,
                editorArea);
        layout.createPlaceholderFolder(MAIN_LEFT_TOP_ID, IPageLayout.TOP, 0.5f,
                MAIN_LEFT_ID);

        IFolderLayout leftBottomFolder = layout.createFolder(
                MAIN_LEFT_BOTTOM_ID, IPageLayout.BOTTOM, 0.5f, MAIN_LEFT_ID);
        leftBottomFolder.addView(IPageLayout.ID_PROP_SHEET);

        // this defines the right part
        layout.createPlaceholderFolder(BOTTOM_RESULTS_ID, IPageLayout.BOTTOM,
                0.6f, editorArea);
        layout.setFixed(false);

        layout.addNewWizardShortcut(NEW_CONNECTION_WIZARD_ID);
    }

}
