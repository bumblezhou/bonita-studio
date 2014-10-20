/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.studio.contract.ui.property.constraint.edit.editor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.bonitasoft.studio.common.emf.tools.ModelHelper;
import org.bonitasoft.studio.common.log.BonitaStudioLog;
import org.bonitasoft.studio.contract.ContractPlugin;
import org.bonitasoft.studio.contract.core.constraint.ConstraintInputIndexer;
import org.bonitasoft.studio.contract.i18n.Messages;
import org.bonitasoft.studio.contract.ui.property.constraint.edit.editor.contentassist.ContractInputCompletionProposalComputer;
import org.bonitasoft.studio.groovy.ui.viewer.GroovyViewer;
import org.bonitasoft.studio.model.process.ContractConstraint;
import org.bonitasoft.studio.model.process.ContractInput;
import org.bonitasoft.studio.model.process.ProcessPackage;
import org.bonitasoft.studio.model.process.Task;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;


/**
 * @author Romain Bioteau
 *
 */
public class ContractConstraintExpressionWizardPage extends WizardPage implements IDocumentListener {

    private static final String MVEL_BASICS_URL = "http://mvel.codehaus.org/Language+Guide+for+2.0";

    private final ContractConstraint constraint;
    private final List<ContractInput> inputs;
    private ConstraintInputIndexer inputIndexer;
    private IObservableValue expressionContentObservable;
    private IObservableList inputsObservable;
    private GroovyViewer groovyViewer;

    public ContractConstraintExpressionWizardPage(final ContractConstraint constraint, final List<ContractInput> inputs) {
        super(ContractConstraintExpressionWizardPage.class.getName());
        setDescription(Messages.constraintEditorDescription);
        this.constraint = constraint;
        this.inputs = inputs;
    }


    @Override
    public void dispose() {
        if (groovyViewer != null) {
            groovyViewer.dispose();
        }
        super.dispose();
    }

    protected SourceViewer getSourceViewer() {
        return groovyViewer.getSourceViewer();
    }

    @Override
    public void createControl(final Composite parent) {
        final EMFDataBindingContext context = new EMFDataBindingContext();
        final Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout());
        final MVELEditor editor = cretaeSourceViewer(container);
        getSourceViewer().getTextWidget().setData(ContractInputCompletionProposalComputer.INPUTS, inputs);
        getSourceViewer().getDocument().addDocumentListener(this);

        setControl(container);
        expressionContentObservable = EMFObservables.observeValue(constraint, ProcessPackage.Literals.CONTRACT_CONSTRAINT__EXPRESSION);

        inputsObservable = EMFObservables.observeList(constraint, ProcessPackage.Literals.CONTRACT_CONSTRAINT__INPUT_NAMES);
        inputIndexer = new ConstraintInputIndexer(inputs, editor.getGroovyCompilationUnit());
        inputIndexer.addJobChangeListener(new UpdateInputReferenceListener(inputsObservable));
        getSourceViewer().getDocument().set(expressionContentObservable.getValue().toString());
        context.addValidationStatusProvider(new ConstraintExpressionEditorValidator(expressionContentObservable, inputsObservable));
        WizardPageSupport.create(this, context);
    }


    protected MVELEditor cretaeSourceViewer(final Composite container) {
        final MVELEditor editor = new MVELEditor();
        groovyViewer = new GroovyViewer(container, null, editor);
        Task parentTask = null;
        if (!inputs.isEmpty()) {
            parentTask = ModelHelper.getFirstContainerOfType(inputs.get(0), Task.class);
        }
        groovyViewer.setContext(null, parentTask, null, null);
        return editor;
    }


    @Override
    public void documentAboutToBeChanged(final DocumentEvent event) {
        //NOTHING TO DO
    }

    @Override
    public void documentChanged(final DocumentEvent event) {
        final String expression = event.getDocument().get();
        expressionContentObservable.setValue(expression);

        if (inputIndexer != null) {
            inputIndexer.setExpression(expression);
            inputIndexer.schedule();
        }
    }


    @Override
    public void performHelp() {
        URL url = null;
        try {
            url = new URL(MVEL_BASICS_URL);
        } catch (final MalformedURLException e) {
            BonitaStudioLog.error("Invalid URL format for :" + MVEL_BASICS_URL, e, ContractPlugin.PLUGIN_ID);
        }
        IWebBrowser browser = null;
        try {
            browser = PlatformUI.getWorkbench().getBrowserSupport().createBrowser(IWorkbenchBrowserSupport.AS_EXTERNAL, null, null, null);
            browser.openURL(url);
        } catch (final PartInitException e) {
            BonitaStudioLog.error("Failed to oepn browser to display contract constraint expression help content", e, ContractPlugin.PLUGIN_ID);
        }
    }
}
