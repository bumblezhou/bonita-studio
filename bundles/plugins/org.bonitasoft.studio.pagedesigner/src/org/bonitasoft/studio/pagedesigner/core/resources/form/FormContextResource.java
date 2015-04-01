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
package org.bonitasoft.studio.pagedesigner.core.resources.form;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.studio.common.emf.tools.ModelHelper;
import org.bonitasoft.studio.common.repository.RepositoryManager;
import org.bonitasoft.studio.diagram.custom.repository.DiagramRepositoryStore;
import org.bonitasoft.studio.model.expression.Expression;
import org.bonitasoft.studio.model.process.AbstractPageFlow;
import org.bonitasoft.studio.model.process.AbstractProcess;
import org.bonitasoft.studio.model.process.BusinessObjectData;
import org.bonitasoft.studio.model.process.Data;
import org.bonitasoft.studio.model.process.Element;
import org.bonitasoft.studio.model.process.FormMapping;
import org.bonitasoft.studio.model.process.PageFlow;
import org.bonitasoft.studio.model.process.Pool;
import org.bonitasoft.studio.model.process.ProcessPackage;
import org.bonitasoft.studio.model.process.RecapFlow;
import org.eclipse.emf.ecore.EClass;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class FormContextResource extends ServerResource {

    private static final String KIND_BUSINESS_DATA = "BusinessData";
    private static final String KEY_DATA_TYPE = "dataType";
    private static final String KEY_KIND = "kind";
    public static final String ATTR_FORMID = "formId";

    @Get("json")
    public JSONObject getFormContext() throws JSONException {
        final String formId = getAttribute(ATTR_FORMID);
        final AbstractPageFlow pageFlow = findPageFlowMappingTo(formId);
        if (pageFlow == null) {
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The form with id " + formId + "is not mapped.");
        }
        final List<BusinessObjectData> context = getContext(pageFlow, formId);
        return transfromToReadableJson(context);
    }

    private JSONObject transfromToReadableJson(final List<BusinessObjectData> context) throws JSONException {
        final JSONObject json = new JSONObject();
        for (final BusinessObjectData businessObjectData : context) {
            final JSONObject businObjectJSOn = new JSONObject();
            businObjectJSOn.put(KEY_KIND, KIND_BUSINESS_DATA);
            businObjectJSOn.put(KEY_DATA_TYPE, businessObjectData.getEClassName());
            json.put(businessObjectData.getName(), businObjectJSOn);
        }
        return json;
    }

    private List<BusinessObjectData> getContext(final AbstractPageFlow pageFlow, final String formId) {
        final List<BusinessObjectData> res = new ArrayList<BusinessObjectData>();
        if (pageFlow instanceof Pool) {
            if (isFormMappingMatchingId(formId, ((PageFlow) pageFlow).getFormMapping())) {
                return res;
            }
        }
        final Pool parentPool = ModelHelper.getParentPool(pageFlow);
        for (final Data data : parentPool.getData()) {
            if (data instanceof BusinessObjectData) {
                res.add((BusinessObjectData) data);
            }
        }
        return res;
    }

    private AbstractPageFlow findPageFlowMappingTo(final String formId) {
        for (final AbstractProcess process : getAllProcesses()) {
            final List<Element> pageFlows = getPageFlowsIn(process);
            for (final Element pageFlow : pageFlows) {
                if (pageFlow instanceof PageFlow
                        && isFormMappingMatchingId(formId, ((PageFlow) pageFlow).getFormMapping())) {
                    return (PageFlow) pageFlow;
                }
                if (pageFlow instanceof RecapFlow
                        && isFormMappingMatchingId(formId, ((RecapFlow) pageFlow).getOverviewFormMapping())) {
                    return (RecapFlow) pageFlow;
                }
            }
        }
        return null;
    }

    private boolean isFormMappingMatchingId(final String formId, final FormMapping formMapping) {
        if (formMapping != null) {
            final Expression targetForm = formMapping.getTargetForm();
            return targetForm != null && formId.equals(targetForm.getContent());
        }
        return false;
    }

    protected List<Element> getPageFlowsIn(final AbstractProcess process) {
        final List<Element> pageFlows = new ArrayList<Element>();
        final List<EClass> types = new ArrayList<EClass>();
        types.add(ProcessPackage.eINSTANCE.getPageFlow());
        types.add(ProcessPackage.eINSTANCE.getRecapFlow());
        ModelHelper.findAllElementsByNature(process, pageFlows, types);
        return pageFlows;
    }

    protected List<AbstractProcess> getAllProcesses() {
        final DiagramRepositoryStore drs = RepositoryManager.getInstance().getRepositoryStore(DiagramRepositoryStore.class);
        return drs.getAllProcesses();
    }

}
