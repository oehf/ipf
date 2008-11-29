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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.openehealth.ipf.platform.manager.connection.ConnectionConfigurationImpl;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.connection.ui.jobs.OpenJMXConnectionJob;
import org.openehealth.ipf.platform.manager.connection.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.connection.ui.utils.encoding.Base64Utils;
import org.openehealth.ipf.platform.manager.connection.ui.utils.messages.Messages;

/**
 * 
 * Wizard for creating new connections
 * <p>
 * 
 * @author Mitko Kolev (i000174)
 */
public class NewConnectionWizard extends Wizard implements INewWizard {

    private IConnectionConfiguration connectionConfigurationObject;

    IWizardPage newWizardPage;

    private boolean shouldConnect = true;

    private static final String WIZARD_TITLE_KEY = "NewConnectionWizardPage.title";

    private static final String IMG_CONNECTION_KEY = "icons/connection/computer_add_64x64.png";

    private final static String CONNECTIONS_MEMENTO__KEY = "connections";

    private final static String lasConnectionHostKey = "lastConnectionHost";

    private final static String lasConnectionPortKey = "lastConnectionPort";

    private final static String lasConnectionNameKey = "lasConnectionName";

    private final static String lasConnectionUserKey = "lastConnection1";

    private final static String lasConnectionPasswordKey = "lastConnection2";

    private Image connectionImage;

    private ImageDescriptor connectionImageDescriptor;

    /**
     * Constructs a new Connection wizard. The pages will initialize its
     * connectionObject.
     * 
     * @param connectionConfiguration
     *            the connection from which to initialize the data.
     */
    public NewConnectionWizard(IConnectionConfiguration connectionConfiguration) {
        super();
        this.connectionConfigurationObject = connectionConfiguration;

        this.setWindowTitle(Messages.getLabelString(WIZARD_TITLE_KEY));
    }

    private void loadImages() {
        connectionImageDescriptor = Activator.getDefault().getImageDescriptor(
                IMG_CONNECTION_KEY);
        if (connectionImageDescriptor != null)
            connectionImage = connectionImageDescriptor.createImage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        IJMXConnectionManager manager = Activator.getDefault()
                .getJMXConnectionManager();
        IWizardPage page = newWizardPage.getNextPage();
        if (page == null) {
            if (connectionConfigurationObject != null) {

                manager
                        .addConnectionConfiguration(connectionConfigurationObject);
            }
        }
        this.saveState();
        if (this.shouldConnect) {
            new OpenJMXConnectionJob(Display.getCurrent(), manager,
                    connectionConfigurationObject).schedule();
        }
        return true;
    }

    /*
     * (non-Javadoc) Method declared on IWizard.
     */
    @Override
    public Image getDefaultPageImage() {
        loadImages();
        if (connectionImage == null) {
            connectionImage = JFaceResources.getResources()
                    .createImageWithDefault(connectionImageDescriptor);
        }
        return connectionImage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }

    @Override
    public void addPages() {
        IConnectionConfiguration lastConnetionPrototype = this.connectionConfigurationObject;
        if (lastConnetionPrototype == null) {
            lastConnetionPrototype = this.readAndSetState();
        }
        if (lastConnetionPrototype != null) {
            newWizardPage = new NewConnectionWizardPage(this,
                    lastConnetionPrototype);
        } else {
            newWizardPage = new NewConnectionWizardPage(this);
        }

        this.addPage(newWizardPage);
    }

    public void setConnectionConfigurationObject(
            IConnectionConfiguration connectionConfiguration) {
        this.connectionConfigurationObject = connectionConfiguration;
    }

    public boolean isShouldConnect() {
        return shouldConnect;
    }

    public void setShouldConnect(boolean shouldConnect) {
        this.shouldConnect = shouldConnect;
    }

    private IConnectionConfiguration readComponentState(IMemento memento) {
        String lastConnectionHost = memento.getString(lasConnectionHostKey);
        if (lastConnectionHost == null) {
            // we have saved no connection
            return null;
        }
        Integer lastConnectionPort = memento.getInteger(lasConnectionPortKey);
        if (lastConnectionPort == null || lastConnectionPort <= 0) {
            // we have saved no connection
            return null;
        }
        String lastConnectionName = memento.getString(lasConnectionNameKey);
        if (lastConnectionName == null) {
            // we have saved no connection
            return null;
        }
        String lastConnectionUserName = memento.getString(lasConnectionUserKey);
        String lastConnectionPassword = memento
                .getString(lasConnectionPasswordKey);

        if (lastConnectionUserName == null || lastConnectionPassword == null) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    lastConnectionName, lastConnectionHost, lastConnectionPort);
            // return connection with no authentication
            return connectionConfiguration;
        }
        final String lastConnectionPasswordDecoded = new String(Base64Utils
                .decode(lastConnectionPassword));
        final String lastConnectionUserNameDecoded = Base64Utils
                .decode(lastConnectionUserName);

        IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                lastConnectionName, lastConnectionHost, lastConnectionPort,
                lastConnectionUserNameDecoded, lastConnectionPasswordDecoded);
        return connectionConfiguration;

    }

    private void writeComponentState(IMemento memento) {
        memento.putString(lasConnectionNameKey,
                this.connectionConfigurationObject.getName());
        memento.putString(lasConnectionHostKey,
                this.connectionConfigurationObject.getHost());
        memento.putInteger(lasConnectionPortKey,
                this.connectionConfigurationObject.getPort());
        if (connectionConfigurationObject.getAuthenticationCredentials()
                .isValid()) {
            String userNameEncoded = Base64Utils
                    .encode(connectionConfigurationObject
                            .getAuthenticationCredentials().getUserName());
            String passwordEncoded = Base64Utils
                    .encode(connectionConfigurationObject
                            .getAuthenticationCredentials().getPassword());
            memento.putString(lasConnectionUserKey, userNameEncoded);
            memento.putString(lasConnectionPasswordKey, passwordEncoded);

        }
    }

    protected synchronized void saveState() {
        XMLMemento memento = XMLMemento
                .createWriteRoot(CONNECTIONS_MEMENTO__KEY);
        IMemento child = memento.createChild(CONNECTIONS_MEMENTO__KEY);
        writeComponentState(child);
        Activator.saveMementoToFile(memento);
    }

    private synchronized IConnectionConfiguration readAndSetState() {
        try {
            XMLMemento memento = XMLMemento.createReadRoot(new BufferedReader(
                    new FileReader(Activator.getStateFile())));
            IMemento thisMemento = memento.getChild(CONNECTIONS_MEMENTO__KEY);
            if (thisMemento != null) {
                return readComponentState(thisMemento);
            }
        } catch (WorkbenchException we) {
            // ignoreit
        } catch (FileNotFoundException fnfe) {
            // ignore
        }
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (connectionImage != null) {
            JFaceResources.getResources().destroyImage(
                    connectionImageDescriptor);
            connectionImage = null;
        }
    }
}
