package org.openehealth.ipf.platform.manager.jmxexplorer.ui.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.MBeanNodeEditor;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.MBeanNodeEditorInput;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanAttributeNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanAttributesGroupNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanOperationNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanOperationsGroupNode;

public class OpenJMXEditorHandler extends AbstractHandler {

    private final Log log = LogFactory.getLog(OpenJMXEditorHandler.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
     * ExecutionEvent)
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPage page = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage();
        ISelection selection;

        if (event != null) {
            selection = HandlerUtil.getCurrentSelection(event);
        } else {
            ISelectionService service = (ISelectionService) PlatformUI
                    .getWorkbench().getActiveWorkbenchWindow().getService(
                            ISelectionService.class);
            selection = service.getSelection();
        }
        MBeanNode node = getMBeanNodeElement(selection);
        if (node == null) {
            return null;
        }
        MBeanNodeEditorInput thisNodeEditorInput = new MBeanNodeEditorInput(
                node);

        try {

            IEditorReference[] references = page.getEditorReferences();
            for (int t = 0; t < references.length; t++) {
                IEditorInput input = references[t].getEditorInput();
                if (input != null) {
                    if (input.equals(thisNodeEditorInput)) {
                        IEditorPart editor = references[t].getEditor(false);
                        page.closeEditor(editor, false);
                        page.openEditor(thisNodeEditorInput,
                                MBeanNodeEditor.class.getName());

                    }
                }
            }
            page.openEditor(thisNodeEditorInput, MBeanNodeEditor.class
                    .getName());

        } catch (PartInitException e1) {
            log.error("Cannto open view", e1);
        }

        return null;
    }

    private MBeanNode getMBeanNodeElement(ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection is = (IStructuredSelection) selection;
            if (is.isEmpty()) {
                return null;
            }
            if (is.size() != 1) {
                return null;
            }
            Object fe = is.getFirstElement();
            if (fe instanceof IAdaptable) {
                IAdaptable adaptable = (IAdaptable) fe;
                Object adapter = adaptable.getAdapter(MBeanNode.class);
                if (adapter instanceof MBeanAttributeNode) {
                    return (MBeanAttributeNode) adapter;
                } else if (adapter instanceof MBeanOperationNode) {
                    return (MBeanOperationNode) adapter;
                } else if (adapter instanceof MBeanOperationsGroupNode) {
                    return (MBeanOperationsGroupNode) adapter;
                } else if (adapter instanceof MBeanAttributesGroupNode) {
                    return (MBeanAttributesGroupNode) adapter;
                }
                // returns the first element
            }
        }

        return null;
    }
}
