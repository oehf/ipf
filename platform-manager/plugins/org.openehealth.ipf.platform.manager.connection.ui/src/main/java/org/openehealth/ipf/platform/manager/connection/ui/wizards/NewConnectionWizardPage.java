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
package org.openehealth.ipf.platform.manager.connection.ui.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.openehealth.ipf.platform.manager.connection.AuthenticationCredentials;
import org.openehealth.ipf.platform.manager.connection.ConnectionConfigurationImpl;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.connection.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.connection.ui.utils.messages.Messages;

/**
 * Page for the new Connection wizard. The class uses the Eclipse usability
 * guidelines to validate itself.
 * <p>
 * 
 * @author Mitko Kolev
 */
public class NewConnectionWizardPage extends WizardPage implements
        SelectionListener {

    private final NewConnectionWizard wizard;

    private Text connectionHostText;

    private Text connectionPortText;

    private Text userNameText;

    private Text passWordText;

    private Text connectionNameText;

    private Button useAuthentication;

    private Button shouldConnectButton;

    private final IConnectionConfiguration lastConnectionConfigurationPrototype;

    private final IJMXConnectionManager jMXConnectionManager;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizardPage#canFlipToNextPage()
     */
    public NewConnectionWizardPage(NewConnectionWizard wizard) {

        this(wizard, null);
    }

    /**
     * Creates a new page which updates the wizard.
     * 
     * @param wizard
     * @param lastConnectionConfiguration
     */
    public NewConnectionWizardPage(NewConnectionWizard wizard,
            IConnectionConfiguration lastConnectionConfiguration) {
        super(Messages.getLabelString("NewConnectionWizardPage.name"));
        this.jMXConnectionManager = Activator.getDefault()
                .getJMXConnectionManager();

        this.wizard = wizard;
        this.setTitle(Messages.getLabelString("NewConnectionWizardPage.title"));
        this.setDescription(Messages
                .getLabelString("NewConnectionWizardPage.description"));
        this.lastConnectionConfigurationPrototype = lastConnectionConfiguration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void createControl(Composite parent) {
        Composite mainComposite = new Composite(parent, SWT.FILL);
        GridLayout mainLayout = new GridLayout();
        mainLayout.numColumns = 2;
        mainLayout.marginHeight = 0;
        mainLayout.marginWidth = 0;
        mainLayout.verticalSpacing = 5;
        mainComposite.setLayout(mainLayout);

        createConnectionProperties(mainComposite);
        this.setControl(mainComposite);
        if (lastConnectionConfigurationPrototype != null) {
            if (jMXConnectionManager
                    .isConnectionNameInUse(lastConnectionConfigurationPrototype
                            .getName())) {
                this.setPageComplete(false);
            } else {
                this.setPageComplete(true);
            }
        } else {
            this.setPageComplete(false);
        }
        this.setPageComplete(canFinish());
    }

    private void createConnectionProperties(Composite parent) {

        Composite mainGroup = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout();
        gl.numColumns = 2;
        mainGroup.setLayout(gl);
        mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
                2));
        CLabel l = new CLabel(mainGroup, SWT.NONE);
        l.setText(Messages
                .getLabelString("NewConnectionWizardPage.purposeText")
                + "\n");
        l.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2));

        CLabel connectionName = new CLabel(mainGroup, SWT.LEFT);
        connectionName.setText(Messages
                .getLabelString("NewConnectionWizardPage.name"));
        connectionName.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
                false, 1, 1));
        connectionNameText = new Text(mainGroup, SWT.BORDER);
        connectionNameText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        connectionNameText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                validateConnectionName();
            }

        });

        CLabel connectionHost = new CLabel(mainGroup, SWT.LEFT);
        connectionHost.setText(Messages
                .getLabelString("NewConnectionWizardPage.connectionHost"));
        connectionHost.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
                false, 1, 1));
        connectionHostText = new Text(mainGroup, SWT.BORDER);
        connectionHostText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        connectionHostText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                validateHostName();
            }

        });

        CLabel connectionPort = new CLabel(mainGroup, SWT.LEFT);
        connectionPort.setText(Messages
                .getLabelString("NewConnectionWizardPage.connectionPort"));
        connectionPort.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
                false, 1, 1));
        connectionPortText = new Text(mainGroup, SWT.BORDER);
        connectionPortText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        connectionPortText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                validatePort();
            }
        });

        Group authenticationGroup = new Group(mainGroup, SWT.NONE);
        authenticationGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
                true, true, 2, 2));
        authenticationGroup.setLayout(new GridLayout(2, false));

        useAuthentication = new Button(authenticationGroup, SWT.CHECK);
        useAuthentication.setText(Messages
                .getLabelString("NewConnectionWizardPage.useAuthentication"));
        useAuthentication.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                false, 2, 1));
        useAuthentication.addSelectionListener(this);

        CLabel userName = new CLabel(authenticationGroup, SWT.LEFT);
        userName.setText(Messages
                .getLabelString("NewConnectionWizardPage.userName"));
        userName.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,
                1, 1));
        userNameText = new Text(authenticationGroup, SWT.BORDER);
        userNameText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        userNameText.setEnabled(false);
        userNameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateUserName();
            }
        });

        CLabel password = new CLabel(authenticationGroup, SWT.LEFT);
        password.setText(Messages
                .getLabelString("NewConnectionWizardPage.password"));
        password.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,
                1, 1));
        passWordText = new Text(authenticationGroup, SWT.BORDER | SWT.PASSWORD);
        passWordText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        passWordText.setEnabled(false);
        passWordText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                validatePassword();
            }

        });

        Group shouldConnectGroup = new Group(mainGroup, SWT.NONE);
        shouldConnectGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 2, 2));
        shouldConnectGroup.setLayout(new GridLayout(2, false));

        shouldConnectButton = new Button(shouldConnectGroup, SWT.CHECK);
        shouldConnectButton.setSelection(true);
        shouldConnectButton.setText(Messages
                .getLabelString("NewConnectionWizardPage.shouldConnect"));

        shouldConnectButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE,
                true, false, 2, 1));

        if (lastConnectionConfigurationPrototype != null) {
            connectionNameText.setText(String
                    .valueOf(lastConnectionConfigurationPrototype.getName()));
            connectionNameText.selectAll();

            connectionHostText.setText(lastConnectionConfigurationPrototype
                    .getHost());
            connectionPortText.setText(String
                    .valueOf(lastConnectionConfigurationPrototype.getPort()));
            if (lastConnectionConfigurationPrototype
                    .getAuthenticationCredentials().isValid()) {
                useAuthentication.setSelection(true);
                userNameText.setEnabled(true);
                passWordText.setEnabled(true);
                userNameText.setText(String
                        .valueOf(lastConnectionConfigurationPrototype
                                .getAuthenticationCredentials().getUserName()));
                passWordText.setText(String
                        .valueOf(lastConnectionConfigurationPrototype
                                .getAuthenticationCredentials().getPassword()));
            } else {
                useAuthentication.setSelection(false);
            }
        }
        this.setErrorMessage(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
     * .swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    protected void validateConnectionName() {
        this.setPageComplete(canFinish());
        NewConnectionWizardPage.this.setErrorMessage(null);
        String name = connectionNameText.getText();
        if (name == null || name.equals("")) {
            return;
        }
        if (jMXConnectionManager.isConnectionNameInUse(name)) {
            NewConnectionWizardPage.this.setErrorMessage(Messages
                    .getLabelString("connection.name.is.already.used.error"));
            return;
        } else {
            NewConnectionWizardPage.this.setErrorMessage(null);
            NewConnectionWizardPage.this.setPageCompleteStatus();
        }
    }

    private void validateHostName() {
        validateConnectionName();
        String host = connectionHostText.getText();
        if (host == null || host.trim().equals("")) {
            return;
        } else {
            NewConnectionWizardPage.this.setErrorMessage(null);
            NewConnectionWizardPage.this.setPageCompleteStatus();
        }
    }

    private void validatePort() {
        validateHostName();
        NewConnectionWizardPage.this.setErrorMessage(null);
        String port = connectionPortText.getText();
        if (port == null || port.trim().equals("")) {
            return;
        }
        try {
            Short.parseShort(port);
            NewConnectionWizardPage.this.setErrorMessage(null);
            NewConnectionWizardPage.this.setPageCompleteStatus();
        } catch (NumberFormatException nfe) {
            NewConnectionWizardPage.this.setErrorMessage(Messages
                    .getLabelString("connection.port.is.invalid.error"));
        }

    }

    private void validateUserName() {
        validatePort();
        NewConnectionWizardPage.this.setErrorMessage(null);
        boolean selected = useAuthentication.getSelection();
        if (selected) {
            String userName = userNameText.getText();
            if (userName == null || userName.trim().equals("")) {
                return;
            }
            NewConnectionWizardPage.this.setErrorMessage(null);
            NewConnectionWizardPage.this.setPageCompleteStatus();
        }
    }

    private void validatePassword() {
        validateUserName();
        NewConnectionWizardPage.this.setErrorMessage(null);
        boolean selected = useAuthentication.getSelection();
        if (selected) {
            String password = passWordText.getText();
            if (password == null || password.trim().equals("")) {
                return;
            }
            NewConnectionWizardPage.this.setErrorMessage(null);
            NewConnectionWizardPage.this.setPageCompleteStatus();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
     * .events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e) {
        Object source = e.getSource();
        if (source.equals(useAuthentication)) {
            boolean checked = useAuthentication.getSelection();
            if (checked) {
                userNameText.setEnabled(true);
                passWordText.setEnabled(true);
            } else {
                userNameText.setEnabled(false);
                userNameText.setText("");
                passWordText.setEnabled(false);
                passWordText.setText("");
            }

            boolean ok = canFinish();
            if (ok) {
                setErrorMessage(null);
            }

            this.setPageComplete(ok);
        }
    }

    protected boolean canFinish() {
        String host = connectionHostText.getText();
        if (host == null || host.trim().equals("")) {
            return false;
        }
        String name = connectionNameText.getText();
        if (name == null || name.trim().equals("")) {
            return false;
        }
        if (jMXConnectionManager.isConnectionNameInUse(name)) {
            return false;
        }

        String port = connectionPortText.getText();
        if (port == null || port.trim().equals("")) {
            return false;
        }
        try {
            Short.parseShort(port);
        } catch (NumberFormatException e) {
            return false;
        }
        boolean selected = useAuthentication.getSelection();
        if (selected) {
            String userName = userNameText.getText();
            if (userName == null || userName.trim().equals("")) {
                return false;
            }
            String password = passWordText.getText();
            if (password == null || password.trim().equals("")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isPageComplete() {
        boolean ok = canFinish();
        return ok;
    }

    /**
     * Sets the page complete status.
     */
    protected void setPageCompleteStatus() {
        boolean ok = canFinish();
        this.setPageComplete(ok);
        if (ok) {
            setErrorMessage(null);
            // this.saveState();
        }
    }

    @Override
    public IWizardPage getNextPage() {
        String sPort = connectionPortText.getText();
        String host = connectionHostText.getText();
        String name = connectionNameText.getText();

        boolean hasAuthentication = useAuthentication.getSelection();
        int port = Integer.parseInt(sPort);
        IConnectionConfiguration connectionConfiguration;
        if (!hasAuthentication) {
            connectionConfiguration = new ConnectionConfigurationImpl(name,
                    host, port);
        } else {
            String userName = userNameText.getText();
            String password = passWordText.getText();
            connectionConfiguration = new ConnectionConfigurationImpl(name,
                    host, port, new AuthenticationCredentials(userName,
                            password));
        }

        wizard.setConnectionConfigurationObject(connectionConfiguration);
        wizard.setShouldConnect(shouldConnectButton.getSelection());
        return super.getNextPage();
    }

}
