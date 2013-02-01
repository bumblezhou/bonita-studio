/**
 * Copyright (C) 2010-2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 *
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
package org.bonitasoft.studio.test.swtbot.util;

import static org.bonitasoft.studio.actors.i18n.Messages.selectActor;
import static org.bonitasoft.studio.actors.i18n.Messages.setAsProcessInitiator;
import static org.bonitasoft.studio.actors.i18n.Messages.useTaskActors;
import static org.bonitasoft.studio.expression.editor.i18n.Messages.editExpression;
import static org.bonitasoft.studio.expression.editor.i18n.Messages.expressionTypeLabel;
import static org.bonitasoft.studio.expression.editor.i18n.Messages.returnType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.bonitasoft.studio.common.ExpressionConstants;
import org.bonitasoft.studio.common.jface.FileActionDialog;
import org.bonitasoft.studio.common.jface.SWTBotConstants;
import org.bonitasoft.studio.common.log.BonitaStudioLog;
import org.bonitasoft.studio.diagram.custom.editPolicies.NextElementEditPolicy;
import org.bonitasoft.studio.diagram.custom.editPolicies.UpdateSizePoolSelectionEditPolicy;
import org.bonitasoft.studio.engine.command.RunProcessCommand;
import org.bonitasoft.studio.expression.editor.viewer.ExpressionViewer;
import org.bonitasoft.studio.model.process.diagram.edit.parts.PoolEditPart;
import org.bonitasoft.studio.model.process.diagram.form.edit.parts.FormEditPart;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.keyboard.Keyboard;
import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyList;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyList.ListElement;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;

public class SWTBotTestUtil implements SWTBotConstants{


    public static final int CONTEXTUALPALETTE_STEP = 0;
    public static final int CONTEXTUALPALETTE_GATEWAY = 1;
    public static final int CONTEXTUALPALETTE_SEQUENCEFLOW = 2;
    //TOOLBAREVENT doesn't work, create a comment
    public static final int CONTEXTUALPALETTE_EVENT= 3;


    public static void createNewDiagram(final SWTWorkbenchBot bot){
        final int nbEditorsBefore = bot.editors().size();
        SWTBotMenu menu = bot.menu("Diagram");
        menu.menu("New").click();
        bot.waitUntil(new ICondition() {

            @Override
            public boolean test() throws Exception {
                return nbEditorsBefore +1 == bot.editors().size() && !bot.activeEditor().isDirty();
            }

            @Override
            public void init(SWTBot bot) {
            }

            @Override
            public String getFailureMessage() {
                return "Editor for new diagram has not been opened or is dirty after new";
            }
        }, 30000);
    }

    public static IStatus selectAndRunFirstPoolFound(final SWTGefBot bot) throws ExecutionException {
        bot.waitUntil(new ICondition() {

            @Override
            public boolean test() throws Exception {
                return  bot.activeEditor() != null;
            }

            @Override
            public void init(SWTBot bot) { }

            @Override
            public String getFailureMessage() {
                return "No active editor found";
            }
        });
        SWTBotEditor botEditor = bot.activeEditor();
        SWTBotGefEditor gmfEditor = bot.gefEditor(botEditor.getTitle());
        List<SWTBotGefEditPart> runnableEPs = gmfEditor.editParts(new BaseMatcher<EditPart>() {

            @Override
            public boolean matches(Object item) {
                return item instanceof PoolEditPart || item instanceof FormEditPart;
            }

            @Override
            public void describeTo(Description description) {

            }
        });
        Assert.assertFalse(runnableEPs.isEmpty());
        gmfEditor.select(runnableEPs.get(0));
        RunProcessCommand cmd =  new RunProcessCommand(true);
        return (IStatus) cmd.execute(null);
    }


    public static boolean testingBosSp() {
        return Platform.getBundle("org.bonitasoft.studioEx.console.libs") != null;
    }


    public static void selectTabbedPropertyView(final SWTBot viewerBot, final String tabeText) {
        viewerBot.sleep(1000); //DO NOT REMOVE ME
        UIThreadRunnable.syncExec(new VoidResult() {
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swtbot.swt.finder.results.VoidResult#run()
             */
            @Override
            @SuppressWarnings("restriction")
            public void run() {
                try {
                    List<? extends Widget> widgets = viewerBot.getFinder().findControls(
                            WidgetMatcherFactory.widgetOfType(TabbedPropertyList.class));
                    Assert.assertTrue(widgets.size() > 0);
                    TabbedPropertyList tabbedPropertyList = (TabbedPropertyList) widgets.get(0);
                    int i = 0;
                    boolean found = false;
                    ListElement currentTab;
                    Method selectMethod = TabbedPropertyList.class.getDeclaredMethod("select", new Class[] { int.class });
                    selectMethod.setAccessible(true);
                    do {
                        currentTab = ((org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyList.ListElement)tabbedPropertyList.getElementAt(i));
                        if (currentTab != null) {
                            String label = currentTab.getTabItem().getText();
                            if (label.equals(tabeText)) {
                                found = true;
                                selectMethod.invoke(tabbedPropertyList, i);
                            }
                        }
                        i++;
                    } while (currentTab != null && !found);
                    if (!found) {
                        throw new WidgetNotFoundException("Can't find a tab item with " + tabeText + " label");
                    }
                } catch (Exception ex) {
                    BonitaStudioLog.error(ex);
                    throw new WidgetNotFoundException("Can't find a tab item with " + tabeText + " label");
                }
            }
        });
    }


    /**
     * @param bot
     * @param resourceNameInClasspath
     * @param importName: type of import (Bonita, xpdl, jpdl, ...)
     * @param diagramEditorTitle
     * @param srcClass
     * @param mustAskOverride
     * @throws IOException
     */
    public static void importProcessWIthPathFromClass(final SWTWorkbenchBot bot, String resourceNameInClasspath, String importName, final String diagramEditorTitle, Class<?> srcClass, boolean mustAskOverride) throws IOException{
        BonitaStudioLog.log("SWTBot begin to import "+ resourceNameInClasspath + " in mode " +importName);
        boolean disable = FileActionDialog.getDisablePopup();
        FileActionDialog.setDisablePopup(true);
        bot.waitUntil(Conditions.widgetIsEnabled(bot.menu("Diagram")),10000);
        SWTBotMenu menu = bot.menu("Diagram");
        menu.menu("Import...").click();

        bot.waitUntil(Conditions.shellIsActive("Import..."));
        URL url = srcClass.getResource(resourceNameInClasspath);
        url = FileLocator.toFileURL(url);
        File file = new File(url.getFile());
        bot.text().setText(file.getAbsolutePath());
        bot.table().select(importName);
        bot.button(IDialogConstants.FINISH_LABEL).click();
        bot.waitUntil(new ICondition() {
            @Override
            public boolean test() throws Exception {
                for (SWTBotEditor aBot : bot.editors()) {
                    if(aBot.getTitle().contains(diagramEditorTitle)) {
                        return true;
                    }
                }

                return false;
            }

            @Override
            public void init(SWTBot bot) {
            }

            @Override
            public String getFailureMessage() {
                return "no active editor";
            }
        },35000);
        FileActionDialog.setDisablePopup(disable);
        BonitaStudioLog.log("SWTBot has imported "+ resourceNameInClasspath + " in mode " +importName);
    }


    /**
     * Create a new Form and save it
     * @param bot
     * @param gmfEditor
     * @param nameOfStepOnwhichCreateTheForm
     * @return
     */
    public static SWTBotEditor createFormWhenOnAProcessWithStep(SWTGefBot bot,
            SWTBotGefEditor gmfEditor, String nameOfStepOnwhichCreateTheForm) {
        SWTBotGefEditPart part = gmfEditor.getEditPart(nameOfStepOnwhichCreateTheForm);
        part.click();
        bot.viewById(VIEWS_PROPERTIES_APPLICATION).show();
        bot.viewById(VIEWS_PROPERTIES_APPLICATION).setFocus();
        SWTBotTestUtil.selectTabbedPropertyView(bot, "Entry pageflow");
        SWTBotView properties = bot.viewById(SWTBotTestUtil.VIEWS_PROPERTIES_APPLICATION);
        properties.bot().button("Add...").click();
        bot.waitUntil(Conditions.shellIsActive("Add form..."));
        bot.button(IDialogConstants.FINISH_LABEL).click();
        SWTBotEditor activeEditor = bot.activeEditor();
        activeEditor.save();
        return activeEditor;
    }

    /**
     * select an event on diagram with the given name
     * @param bot
     * @param gmfEditor
     * @param poolName
     * @param eventName
     */
    public static void selectEventOnProcess(final  SWTGefBot bot, SWTBotGefEditor gmfEditor,String eventName){
        SWTBotGefEditPart event = gmfEditor.getEditPart(eventName).parent();
        event.click();
    }

    /**
     * select an event in the overview tree. Becarefull, if treeViewer exists in other views SWTBot may not find the one in overview
     * @param bot
     * @param poolName
     * @param eventName
     */
    public static void selectElementFromOverview(final  SWTGefBot bot,String poolName,String laneName, String eventName){
        final SWTBotView view = bot.viewById(SWTBotTestUtil.VIEWS_OVERVIEW);
        view.show();
        view.setFocus();
        view.toolbarButton("Outline").click();
        final SWTBotTree tree = bot.treeWithId(BONITA_OVERVIEW_TREE_ID);
        tree.setFocus();
        tree.getTreeItem("Pool "+poolName).click();
        if (laneName==null){
            tree.expandNode("Pool "+poolName).select(eventName);
        } else {
            tree.expandNode("Lane "+laneName).click();
        }
    }

    /**
     * @param gmfEditor
     * @param selectedElementName
     * @param elementIndex
     * @param isEvent
     * @param dropLocation
     */
    public static void selectElementInContextualPaletteAndDragIt(SWTBotGefEditor gmfEditor,String selectedElementName,int elementIndex, Point dropLocation){
        SWTBotGefEditPart element ;
        SWTBotGefEditPart gep = gmfEditor.getEditPart(selectedElementName);
        Assert.assertNotNull("Error: No Edit Part \'"+selectedElementName+"\' found.", gep);
        element = gep.parent();
        element.select();
        IGraphicalEditPart graphicalEditPart = (IGraphicalEditPart) element.part();
        NextElementEditPolicy nextElementEditPolicy = (NextElementEditPolicy)graphicalEditPart.getEditPolicy(NextElementEditPolicy.NEXT_ELEMENT_ROLE);

        IFigure toolbarFigure =nextElementEditPolicy.getFigure(elementIndex);
        Point location = toolbarFigure.getBounds().getCenter().getCopy();
        toolbarFigure.translateToAbsolute(location);
        gmfEditor.drag(location.x, location.y, dropLocation.x,dropLocation.y);
    }

    /**
     * 
     * @param gmfEditor
     * @param selectedElementName
     * @param dropLocation
     */
    public static void selectTaskFromSelectedElementAndDragIt(SWTBotGefEditor gmfEditor,String selectedElementName, Point dropLocation){
        selectElementInContextualPaletteAndDragIt( gmfEditor, selectedElementName, CONTEXTUALPALETTE_STEP, dropLocation);
    }

    /**
     * 
     * @param gmfEditor
     * @param selectedElementName
     * @param dropLocation
     */
    public static void selectTransitionFromSelectedElementAndDragIt(SWTBotGefEditor gmfEditor,String selectedElementName, Point dropLocation){
        selectElementInContextualPaletteAndDragIt( gmfEditor, selectedElementName, CONTEXTUALPALETTE_SEQUENCEFLOW, dropLocation);
    }


    /**
     * add data wizard configuration (only for defined types as String Integer Boolean etc.)
     * @param bot
     * @param name
     * @param type
     * @param multiplicity
     * @param defaultValue
     */
    public static void addNewData(SWTBot bot,String name, String type, boolean multiplicity,String defaultValue){
        bot.waitUntil(Conditions.shellIsActive("New variable"));
        bot.textWithLabel(org.bonitasoft.studio.properties.i18n.Messages.name).setText(name);
        bot.comboBoxWithLabel(org.bonitasoft.studio.properties.i18n.Messages.datatypeLabel).setSelection(type);
        if (multiplicity){
            bot.checkBox("Is multiple").select();
        }
        if (defaultValue!=null){
            bot.textWithLabel(org.bonitasoft.studio.properties.i18n.Messages.defaultValueLabel).setText(defaultValue);
        }
        bot.button(IDialogConstants.FINISH_LABEL).click();
    }

    /**
     * add data with option wizard configuration (only for defined types as String Integer Boolean etc.)
     * @param bot
     * @param name
     * @param type
     * @param multiplicity
     * @param defaultValue
     */
    public static void addCustomData(SWTBot bot,String name,String type,Map<String,List<String>> options,boolean isMultiple,String defaultValue){
        bot.waitUntil(Conditions.shellIsActive("New variable"));
        bot.textWithLabel(org.bonitasoft.studio.properties.i18n.Messages.name).setText(name);
        bot.comboBoxWithLabel(org.bonitasoft.studio.properties.i18n.Messages.datatypeLabel).setSelection(type);
        bot.button("List of options...").click();
        bot.waitUntil(Conditions.shellIsActive("List of options"));
        int i =0;
        for (String optionsName:options.keySet()){
            bot.button("Add", 0).click();
            bot.table(0).click(i, 0);
            bot.text().setText(optionsName);
            int j=0;
            for (String option:options.get(optionsName)){
                bot.button("Add",1).click();
                bot.table(1).click(j, 0);
                bot.text().setText(option);
                j++;
            }
            i++;
        }
        bot.button(IDialogConstants.OK_LABEL).click();
        if (isMultiple){
            bot.checkBox("Is multiple").select();
        }
        if (defaultValue!=null){
            bot.textWithLabel(org.bonitasoft.studio.properties.i18n.Messages.defaultValueLabel).setText(defaultValue);
            bot.sleep(1000);
        }
        bot.button(IDialogConstants.FINISH_LABEL).click();
    }


    /**
     * add a sequence flow between startElement and End Element
     * @param bot
     * @param gmfEditor
     * @param startElementName
     * @param endElementName
     */
    public static void addSequenceFlow(SWTGefBot bot,SWTBotGefEditor gmfEditor,String startElementName,String endElementName){
        final IGraphicalEditPart gep =  (IGraphicalEditPart) gmfEditor.getEditPart(endElementName).parent().part();
        IFigure figure = gep.getFigure();
        Rectangle dest = figure.getBounds().getCopy();
        figure.translateToAbsolute(dest);
        selectElementInContextualPaletteAndDragIt(gmfEditor, startElementName, CONTEXTUALPALETTE_SEQUENCEFLOW, dest.getLocation());
    }



    public Point getLocationOfElementInDiagram(SWTGefBot bot,SWTBotGefEditor gmfEditor,String elementName){
        final IGraphicalEditPart gep =  (IGraphicalEditPart) gmfEditor.getEditPart(elementName).parent().part();
        IFigure figure = gep.getFigure();
        Rectangle dest = figure.getBounds().getCopy();
        figure.translateToAbsolute(dest);
        return dest.getLocation();
    }


    /**
     * 
     * @param bot
     * @param taskName
     * @param newName
     */
    public static void changeDiagramName(SWTGefBot bot, String taskName, String newName){

        SWTBotEditor botEditor = bot.activeEditor();
        SWTBotGefEditor gmfEditor = bot.gefEditor(botEditor.getTitle());

        //	    AbstractProcess proc = ModelHelper.getParentProcess(((IGraphicalEditPart)gmfEditor.getEditPart(taskName).part()).resolveSemanticElement());
        //	    AbstractProcess diag = ModelHelper.getParentProcess(((IGraphicalEditPart)gmfEditor.getEditPart(proc.getName()).part()).resolveSemanticElement());
        //	    AbstractProcess diag = ModelHelper.getMainProcess(((IGraphicalEditPart)gmfEditor.getEditPart("Step1").part()).resolveSemanticElement());

        gmfEditor.mainEditPart().click();

        bot.viewById(SWTBotTestUtil.VIEWS_PROPERTIES_PROCESS_GENERAL).show();
        bot.viewById(SWTBotTestUtil.VIEWS_PROPERTIES_PROCESS_GENERAL).setFocus();

        selectTabbedPropertyView(bot, "Diagram");
        bot.waitUntil(Conditions.widgetIsEnabled(bot.button("Edit...")));
        bot.button("Edit...").click();


        // Open new Shell
        bot.waitUntil(Conditions.shellIsActive(org.bonitasoft.studio.common.Messages.openNameAndVersionDialogTitle));

        bot.textWithLabel(org.bonitasoft.studio.common.Messages.name,0).setText(newName);
        bot.button(IDialogConstants.OK_LABEL).click();

    }

    /**
     * select in the overview tree the first transition flow  in the given pool
     * @param name
     * @param defaultFlow
     * @param condition
     */
    public static void configureSequenceFlow(SWTGefBot bot,String name,final String pool,boolean defaultFlow,String condition,String expressionType){
        bot.activeEditor().setFocus();
        final SWTBotView view = bot.viewById(SWTBotTestUtil.VIEWS_OVERVIEW);
        view.show();
        view.setFocus();
        view.toolbarButton("Outline").click();
        final SWTBotTree overviewTree = bot.tree();
        bot.waitUntil(Conditions.widgetIsEnabled(overviewTree));
        overviewTree.setFocus();
        bot.waitUntil(new ICondition() {

            @Override
            public boolean test() throws Exception {
                SWTBotTreeItem item = null;
                try{
                    item = overviewTree.getTreeItem("Pool "+pool);
                }catch (TimeoutException e) {
                    return false;
                }
                return item != null;
            }

            @Override
            public void init(SWTBot bot) {  }

            @Override
            public String getFailureMessage() {
            	StringBuilder res= new StringBuilder();
            	for(SWTBotTreeItem sti : overviewTree.getAllItems()){
            		res.append(sti.getText()+"\n");
            	}
                return "Pool "+pool +" not found in overview tree.\n"+res;
            }
        },10000,500);
        final SWTBotTreeItem treeItem = overviewTree.getTreeItem("Pool "+pool);
        if(!treeItem.isExpanded()){
            treeItem.expand();
        }
        treeItem.select("Sequence Flow");
        selectTabbedPropertyView(bot, "General");
        bot.waitUntil(Conditions.widgetIsEnabled(bot.textWithLabel("Condition")));
        bot.textWithLabel("Name").setText(name);
        bot.sleep(1000);
        if (defaultFlow) {
            bot.checkBox("Default flow").select();
        }
        //ExpressionConstants.VARIABLE_TYPE
        if (condition!=null ){
            if(expressionType==ExpressionConstants.VARIABLE_TYPE){
                // edit button
                bot.toolbarButtonWithId(ExpressionViewer.SWTBOT_ID_EDITBUTTON, 0).click();
                setVariableExpression( bot, condition );
            }else if(expressionType==ExpressionConstants.SCRIPT_TYPE){
                // edit button
                bot.toolbarButtonWithId(ExpressionViewer.SWTBOT_ID_EDITBUTTON, 0).click();
                //edit expression shell
                setScriptExpression(bot, "script_"+name, condition, null);
                bot.sleep(600);
            }else if(expressionType==ExpressionConstants.CONSTANT_TYPE){
                bot.textWithLabel("Condition").setText(condition);
                bot.sleep(600);
            }else{
                Assert.assertTrue("Error: Expression type "+expressionType+  " is not supported.", false);
            }
        }



    }

    public static Keyboard getKeybord(){
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
        return KeyboardFactory.getSWTKeyboard();
    }

    public static void pressEnter(){
        getKeybord().pressShortcut(Keystrokes.CR);
    }

    public static void pressDelete(){
        getKeybord().pressShortcut(Keystrokes.DELETE);
    }

    /**
     * 
     * @param scriptName
     * @param expression
     * @param returnTypeOfScript
     */
    public static void setScriptExpression(SWTGefBot bot, String scriptName, String expression, String returnTypeOfScript ){
        bot.waitUntil(Conditions.shellIsActive(editExpression));
        bot.tableWithLabel(expressionTypeLabel).select("Script");
        bot.sleep(1000);
        // set the Script name
        bot.textWithLabel("Name *").setText(scriptName);
        bot.styledText().setText(expression);
        if(returnTypeOfScript!=null){
            bot.comboBoxWithLabel(returnType).setText(returnTypeOfScript);
        }
        bot.waitUntil(Conditions.widgetIsEnabled(bot.button(IDialogConstants.OK_LABEL)));
        bot.button(IDialogConstants.OK_LABEL).click();
    }

    /**
     * 
     * @param bot
     * @param variableName
     */
    public static void setVariableExpression(SWTGefBot bot, String variableName ){
        bot.waitUntil(Conditions.shellIsActive(editExpression));
        bot.tableWithLabel(expressionTypeLabel).select("Variable");
        bot.sleep(1000);
        // select the variable
        SWTBotTable tableVar = bot.table(1);
        for(int i =0;i<tableVar.rowCount();i++ ){
            if(tableVar.getTableItem(i).getText().startsWith(variableName+" --")){
                tableVar.getTableItem(i).select();
                break;
            }
        }
        bot.waitUntil(Conditions.widgetIsEnabled(bot.button(IDialogConstants.OK_LABEL)));
        bot.button(IDialogConstants.OK_LABEL).click();
    }

    /** Select an actor in a Human task in the list of Process Actor
     * 
     * @param bot
     * @param actor
     */
    public static void selectActorInHumanTask(SWTGefBot bot, String actor){
        bot.viewById(VIEWS_PROPERTIES_PROCESS_GENERAL).show();
        selectTabbedPropertyView(bot, "Actors");
        // "Use below actor"
        bot.radio(useTaskActors).click();
        // "Select Actor"
        bot.comboBoxWithLabel(selectActor).setSelection(actor);
    }

    /** Select an actor and define it as an initiator
     * 
     * @param bot
     * @param actor
     */
    public static void selectInitiatorinActor(SWTGefBot bot, String actor){
        bot.viewById(VIEWS_PROPERTIES_PROCESS_GENERAL).show();
        selectTabbedPropertyView(bot, "Actors");
        if(bot.button(setAsProcessInitiator).isEnabled()){
            int actorIdx = bot.table().indexOf(actor, org.bonitasoft.studio.actors.i18n.Messages.name);
            Assert.assertTrue("Error: no actor "+actor+" available", actorIdx!=-1);
            bot.table().getTableItem(actorIdx).select();
            bot.button(setAsProcessInitiator).click();
        }
    }

    /** Wait for the shell with its title name with the default timeout
     * 
     * @param bot
     * @param shellTitle
     */
    public static void waitForActiveShell(SWTGefBot bot, String shellTitle){
        bot.waitUntil(Conditions.shellIsActive(shellTitle));
    }

    public static void increasePoolWidth(SWTBotGefEditor editor,String poolName){
        SWTBotGefEditPart poolPart = editor.getEditPart(poolName).parent();
        poolPart.select();

        IGraphicalEditPart graphicalEditPart = (IGraphicalEditPart) poolPart.part();
        UpdateSizePoolSelectionEditPolicy addPoolSizeElementEditPolicy = (UpdateSizePoolSelectionEditPolicy)graphicalEditPart.getEditPolicy(UpdateSizePoolSelectionEditPolicy.UPDATE_POOL_SIZE_SELECTION_FEEDBACK_ROLE);

        IFigure toolbarFigure = addPoolSizeElementEditPolicy.getFigure(UpdateSizePoolSelectionEditPolicy.ADD_RIGHT);
        Point location = toolbarFigure.getBounds().getCenter().getCopy();
        toolbarFigure.translateToAbsolute(location);
        editor.click(location.x, location.y);
    }

    public static void increasePoolHeight(SWTBotGefEditor editor,String poolName){
        SWTBotGefEditPart poolPart = editor.getEditPart(poolName).parent();
        poolPart.select();

        IGraphicalEditPart graphicalEditPart = (IGraphicalEditPart) poolPart.part();
        UpdateSizePoolSelectionEditPolicy addPoolSizeElementEditPolicy = (UpdateSizePoolSelectionEditPolicy)graphicalEditPart.getEditPolicy(UpdateSizePoolSelectionEditPolicy.UPDATE_POOL_SIZE_SELECTION_FEEDBACK_ROLE);

        IFigure toolbarFigure = addPoolSizeElementEditPolicy.getFigure(UpdateSizePoolSelectionEditPolicy.ADD_BOTTOM);
        Point location = toolbarFigure.getBounds().getCenter().getCopy();
        toolbarFigure.translateToAbsolute(location);
        editor.click(location.x, location.y);
    }    
    
    /**
     * 
     * @param bot
     */
    public static void editScriptConnector(SWTGefBot bot, String scriptName, String scriptText){
    	editScriptConnector( bot,  scriptName,  scriptText, null );
    }
    
   /**
     * 
     * @param bot
     */
    public static void editScriptConnector(SWTGefBot bot, String scriptName, String scriptText, String scriptDescription ){
    	// 1st page
    	editConnector( bot,"Script", "Groovy");
    	
    	// 2nde page
    	bot.textWithLabel("Name *").setText(scriptName);
    	if(scriptDescription!=null){
    		bot.textWithLabel("Description").setText(scriptDescription);
    	}
    	Assert.assertTrue("Error : Next button is not enable in Connectors Wizard.", bot.button(IDialogConstants.NEXT_LABEL).isEnabled());
    	bot.button(IDialogConstants.NEXT_LABEL).click();
    	
    	// 3th page
    	bot.styledText().setText(scriptText);
    	Assert.assertTrue("Error : Next button is not enable in Connectors Wizard.", bot.button(IDialogConstants.NEXT_LABEL).isEnabled());
    	bot.button(IDialogConstants.NEXT_LABEL).click();
    	
    	// 4th page
    	Assert.assertTrue("Error : finish button is not enable in Connectors Wizard.", bot.button(IDialogConstants.FINISH_LABEL).isEnabled());
    	bot.button(IDialogConstants.FINISH_LABEL).click();
    }
    
    /** Set the connector type and the select the connector specified in the parameters
     * 
     * @param bot
     * @param connectorType
     * @param connectorTool
     */
    public static void editConnector(SWTGefBot bot, String connectorType, String connectorTool){
    	
    	bot.waitUntil(Conditions.shellIsActive("Connectors"));
    	SWTBotTree tree = bot.tree();
    	tree.expandNode(connectorType);
    	SWTBotTreeItem theItem = tree.getTreeItem(connectorType);
    	Assert.assertNotNull("Error : No item "+connectorType+" found in the tree.", theItem);
    	for( SWTBotTreeItem item : theItem.getItems()){
    		System.out.println("item = "+item.getText());
    		if(item.getText().startsWith(connectorTool)){
    			item.select();
    			item.click();
    			break;
    		}
    	}
    	
    	Assert.assertTrue("Error : No "+ connectorTool +" "+connectorType +" found in the connector list", bot.button(IDialogConstants.NEXT_LABEL).isEnabled());
    	
    	bot.button(IDialogConstants.NEXT_LABEL).click();
    	
    }

	public static void editEmailConnector(SWTGefBot bot, String emailName, String emailDescription, String from, String to, String subject, String message ) {
    	// 1st page
    	editConnector( bot,"Messaging", "Email");
    	
    	// 2nde page
    	bot.textWithLabel("Name *").setText(emailName);
    	if(emailDescription!=null && !emailDescription.isEmpty()){
    		bot.textWithLabel("Description").setText(emailDescription);
    	}
    	Assert.assertTrue("Error : Next button is not enable in Connectors Wizard.", bot.button(IDialogConstants.NEXT_LABEL).isEnabled());
    	bot.button(IDialogConstants.NEXT_LABEL).click();
    	
    	// 3th page
    	Assert.assertTrue("Error : Next button is not enable in Connectors Wizard.", bot.button(IDialogConstants.NEXT_LABEL).isEnabled());
    	bot.button(IDialogConstants.NEXT_LABEL).click();
    	
    	// 4th page
    	bot.textWithLabel("From *").setText(from);
    	bot.textWithLabel("To *").setText(to);
    	bot.waitUntil(Conditions.widgetIsEnabled(bot.button(IDialogConstants.NEXT_LABEL)));
    	Assert.assertTrue("Error : Next button is not enable in Connectors Wizard.", bot.button(IDialogConstants.NEXT_LABEL).isEnabled());
    	bot.button(IDialogConstants.NEXT_LABEL).click();
    	
    	// 5th page
    	bot.textWithLabel("Subject *").setText(subject);
    	if(message!=null && !message.isEmpty()){
    		bot.textWithLabel("Message").setText(message);
    	}
    	bot.waitUntil(Conditions.widgetIsEnabled(bot.button(IDialogConstants.NEXT_LABEL)));
    	Assert.assertTrue("Error : Next button is not enable in Connectors Wizard.", bot.button(IDialogConstants.NEXT_LABEL).isEnabled());
    	bot.button(IDialogConstants.NEXT_LABEL).click();
    	
    	// 6th page
    	Assert.assertTrue("Error : Next button is not enable in Connectors Wizard.", bot.button(IDialogConstants.NEXT_LABEL).isEnabled());
    	bot.button(IDialogConstants.NEXT_LABEL).click();
    	
    	// 7th page
    	Assert.assertTrue("Error : Next button is not enable in Connectors Wizard.", bot.button(IDialogConstants.FINISH_LABEL).isEnabled());
    	bot.button(IDialogConstants.FINISH_LABEL).click();
     	
    	
	}
}