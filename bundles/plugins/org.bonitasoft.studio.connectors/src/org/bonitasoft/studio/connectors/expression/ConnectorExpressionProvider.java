/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.studio.connectors.expression;

import java.util.Collections;
import java.util.Set;

import org.bonitasoft.studio.common.ExpressionConstants;
import org.bonitasoft.studio.common.repository.RepositoryManager;
import org.bonitasoft.studio.connector.model.definition.ConnectorDefinition;
import org.bonitasoft.studio.connectors.repository.ConnectorDefRepositoryStore;
import org.bonitasoft.studio.expression.editor.provider.IExpressionEditor;
import org.bonitasoft.studio.expression.editor.provider.IExpressionProvider;
import org.bonitasoft.studio.model.expression.Expression;
import org.bonitasoft.studio.model.process.Connector;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;

/**
 * @author Florine Boudin
 *
 */
public class ConnectorExpressionProvider implements IExpressionProvider {

	private final ConnectorDefRepositoryStore store;

	public ConnectorExpressionProvider(){
		store = (ConnectorDefRepositoryStore) RepositoryManager.getInstance().getRepositoryStore(ConnectorDefRepositoryStore.class);
	}

	@Override
	public Set<Expression> getExpressions(EObject context) {
		return Collections.emptySet();
	}

	private EObject getConnectorDefinition(EObject context) {
		EObject definition = context ;
		if(context != null){
			while (definition != null && !(definition instanceof ConnectorDefinition)) {
				if(definition instanceof Connector){
					String defId =   ((Connector) definition).getDefinitionId() ;
					String defVersion =   ((Connector) definition).getDefinitionVersion() ;
					definition = store.getDefinition(defId, defVersion) ;
					break ;
				}
				definition = definition.eContainer() ;
			}
		}
		return definition;
	}



	@Override
	public String getExpressionType() {
		return ExpressionConstants.CONNECTOR_TYPE;
	}

	@Override
	public Image getIcon(Expression expression) {
		if(expression.getConnectors().isEmpty()){
			return null ;
		}
		final Connector connector = expression.getConnectors().get(0);
		final ConnectorDefinition def = (ConnectorDefinition) getConnectorDefinition(connector);
		if(def != null){
			return store.getResourceProvider().getDefinitionIcon(def);
		}
		return null;
	}

	@Override
	public String getProposalLabel(Expression expression) {
		return expression.getName() ;
	}

	@Override
	public boolean isRelevantFor(EObject context) {
		return false;
	}

	@Override
	public Image getTypeIcon() {
		return null;
	}

	@Override
	public String getTypeLabel() {
		return "";
	}

	@Override
	public IExpressionEditor getExpressionEditor(Expression expression,EObject context) {
		return null;
	}



}
