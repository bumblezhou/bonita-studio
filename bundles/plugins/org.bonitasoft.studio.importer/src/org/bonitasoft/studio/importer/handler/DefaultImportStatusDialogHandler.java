/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.studio.importer.handler;

import org.bonitasoft.studio.common.jface.FileActionDialog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Romain Bioteau
 */
public class DefaultImportStatusDialogHandler implements ImportStatusDialogHandler {

    protected final IStatus importStatus;

    public DefaultImportStatusDialogHandler(final IStatus importStatus) {
        this.importStatus = importStatus;
    }

    @Override
    public void open(final Shell parentShell) {
        //Dot not display during tests
        if (!FileActionDialog.getDisablePopup()) {
            if (importStatus.isOK()) {
                MessageDialog.openInformation(parentShell, org.bonitasoft.studio.importer.i18n.Messages.importResultTitle, getDialogMessage(importStatus));
            } else {
                openError(parentShell);
            }
        }
    }

    protected void openError(final Shell parentShell) {
        new ImportErrorMessageDialog(parentShell, getDialogMessage(importStatus),
                false).open();
    }

    protected String getDialogMessage(final IStatus status) {
        if (status.isOK()) {
            return org.bonitasoft.studio.importer.i18n.Messages.importSucessfulMessage;
        }
        if (status instanceof MultiStatus) {
            return createMessageForMultiStatus((MultiStatus) status);
        }
        return status.getMessage();
    }

    protected String createMessageForMultiStatus(final MultiStatus status) {
        final StringBuilder sb = new StringBuilder();
        for (final IStatus childStatus : status.getChildren()) {
            if (!childStatus.isOK()) {
                sb.append(childStatus.getMessage());
                sb.append(SWT.CR);
            }
        }
        return sb.toString();
    }

}
