/**
 * 
 */
package org.bonitasoft.studio.properties.sections.index;

import java.util.HashSet;
import java.util.Set;

import org.bonitasoft.studio.common.ExpressionConstants;
import org.bonitasoft.studio.common.emf.tools.ModelHelper;
import org.bonitasoft.studio.expression.editor.provider.IExpressionEditor;
import org.bonitasoft.studio.expression.editor.provider.IExpressionProvider;
import org.bonitasoft.studio.model.expression.Expression;
import org.bonitasoft.studio.model.expression.ExpressionFactory;
import org.bonitasoft.studio.model.process.Pool;
import org.bonitasoft.studio.model.process.SearchIndex;
import org.bonitasoft.studio.properties.i18n.Messages;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.Image;

/**
 * @author aurelie
 *
 */
public class SearchIndexExpressionProvider implements IExpressionProvider {

	/* (non-Javadoc)
	 * @see org.bonitasoft.studio.expression.editor.provider.IExpressionProvider#getExpressions(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public Set<Expression> getExpressions(EObject context) {
		Set<Expression> result = new HashSet<Expression>();
		Pool pool = (Pool)ModelHelper.getParentProcess(context);
		if (pool!=null){
			for(SearchIndex searchIndex:pool.getSearchIndexs()){
				if (!searchIndex.getName().getContent().trim().isEmpty()){
					result.add(createExpression(searchIndex));
				}
			}
		}
		return result;
	}

	private Expression createExpression(SearchIndex searchIndex){
		 Expression exp = ExpressionFactory.eINSTANCE.createExpression() ;
		 exp.setType(getExpressionType());
		 exp.setName(searchIndex.getName().getContent());
		 exp.setContent(searchIndex.getName().getContent());
		 exp.setType(getExpressionType());
		 exp.getReferencedElements().add(EcoreUtil.copy(searchIndex));
		 return exp;
	}
	
	/* (non-Javadoc)
	 * @see org.bonitasoft.studio.expression.editor.provider.IExpressionProvider#getExpressionType()
	 */
	@Override
	public String getExpressionType() {
		return ExpressionConstants.SEARCH_INDEX_TYPE;
	}

	/* (non-Javadoc)
	 * @see org.bonitasoft.studio.expression.editor.provider.IExpressionProvider#getIcon(org.bonitasoft.studio.model.expression.Expression)
	 */
	@Override
	public Image getIcon(Expression expression) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.bonitasoft.studio.expression.editor.provider.IExpressionProvider#getTypeIcon()
	 */
	@Override
	public Image getTypeIcon() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.bonitasoft.studio.expression.editor.provider.IExpressionProvider#getProposalLabel(org.bonitasoft.studio.model.expression.Expression)
	 */
	@Override
	public String getProposalLabel(Expression expression) {
		  return expression.getName();
	}

	/* (non-Javadoc)
	 * @see org.bonitasoft.studio.expression.editor.provider.IExpressionProvider#isRelevantFor(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public boolean isRelevantFor(EObject context) {
		 return true;
	}

	/* (non-Javadoc)
	 * @see org.bonitasoft.studio.expression.editor.provider.IExpressionProvider#getTypeLabel()
	 */
	@Override
	public String getTypeLabel() {
		// TODO Auto-generated method stub
		return Messages.searchIndexTypeLabel;
	}

	/* (non-Javadoc)
	 * @see org.bonitasoft.studio.expression.editor.provider.IExpressionProvider#getExpressionEditor(org.bonitasoft.studio.model.expression.Expression)
	 */
	@Override
	public IExpressionEditor getExpressionEditor(Expression expression) {
		// TODO Auto-generated method stub
		return null;
	}

}
