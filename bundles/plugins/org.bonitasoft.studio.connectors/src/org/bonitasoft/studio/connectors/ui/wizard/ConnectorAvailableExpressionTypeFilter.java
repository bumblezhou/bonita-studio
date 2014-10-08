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
package org.bonitasoft.studio.connectors.ui.wizard;

import java.util.HashSet;
import java.util.Set;

import org.bonitasoft.engine.bpm.connector.ConnectorEvent;
import org.bonitasoft.studio.common.ExpressionConstants;
import org.bonitasoft.studio.expression.editor.filter.AvailableExpressionTypeFilter;
import org.bonitasoft.studio.model.process.Connector;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;


/**
 * @author Romain Bioteau
 *
 */
public class ConnectorAvailableExpressionTypeFilter extends AvailableExpressionTypeFilter {

    public ConnectorAvailableExpressionTypeFilter(final String[] contentTypes) {
        super(contentTypes);
    }

    public ConnectorAvailableExpressionTypeFilter() {
        super(new String[] {
                ExpressionConstants.CONSTANT_TYPE,
                ExpressionConstants.VARIABLE_TYPE,
                ExpressionConstants.SCRIPT_TYPE,
                ExpressionConstants.PARAMETER_TYPE
        });
    }

    @Override
    public boolean select(final Viewer viewer, final Object context, final Object element) {
        if (viewer != null) {
            final Connector connector = getParentConnector(viewer.getInput());
            if (connector != null && connector.getEvent().equals(ConnectorEvent.ON_FINISH.name())) {
                final Set<String> contentTypes = new HashSet<String>(getContentTypes());
                contentTypes.add(ExpressionConstants.CONTRACT_INPUT_TYPE);
                return isExpressionAllowed(element, contentTypes);
            }
        }
        return super.select(viewer, context, element);
    }

    private Connector getParentConnector(final Object context) {
        if(context instanceof EObject){
            EObject current = (EObject) context;
            while (current != null && !(current instanceof Connector)) {
                current = current.eContainer();
            }
            return (Connector) current;
        }
        return null;
    }

}
