/**
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.studio.tests.pagedesigner;

import org.bonitasoft.studio.swtbot.framework.application.BotApplicationWorkbenchWindow;
import org.bonitasoft.studio.swtbot.framework.diagram.BotProcessDiagramPerspective;
import org.eclipse.swtbot.eclipse.gef.finder.SWTBotGefTestCase;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class PageDesignerContextIT extends SWTBotGefTestCase {

    @Test
    public void testContextOfOpenedNotSavedEditorUsed() {
        final BotApplicationWorkbenchWindow botApplicationWorkbenchWindow = new BotApplicationWorkbenchWindow(bot);
        final BotProcessDiagramPerspective botProcessDiagramPerspective = botApplicationWorkbenchWindow.createNewDiagram();

        //check result return 0

        //ensure a Business Object is defined
        //        final BusinessObjectModelRepositoryStore bomrs = RepositoryManager.getInstance().getRepositoryStore(BusinessObjectModelRepositoryStore.class);
        //        if (bomrs.getChildren().isEmpty()) {
        //            final BusinessObjectModelFileStore store = bomrs.createRepositoryFileStore("test");
        //            final BusinessObjectModel bom = new BusinessObjectModel();
        //            final BusinessObject bo = new BusinessObject();
        //            final Field field = new SimpleField();
        //            field.setName("fieldName");
        //            bo.addField(field);
        //            bom.getBusinessObjects().add(bo);
        //            store.save(content);
        //        }

        botProcessDiagramPerspective.getDiagramPropertiesPart().selectGeneralTab().selectDataTab().addBusinessData().setName("BusinessData").finish();

        //check result contains one element
    }

}
