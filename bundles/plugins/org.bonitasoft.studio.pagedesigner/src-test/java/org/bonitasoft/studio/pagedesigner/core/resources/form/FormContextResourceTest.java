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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.studio.common.emf.tools.ExpressionHelper;
import org.bonitasoft.studio.model.expression.builders.ExpressionBuilder;
import org.bonitasoft.studio.model.process.AbstractProcess;
import org.bonitasoft.studio.model.process.PageFlow;
import org.bonitasoft.studio.model.process.builders.BusinessObjectDataBuilder;
import org.bonitasoft.studio.model.process.builders.FormMappingBuilder;
import org.bonitasoft.studio.model.process.builders.JavaDataTypeBuilder;
import org.bonitasoft.studio.model.process.builders.JavaObjectDataBuilder;
import org.bonitasoft.studio.model.process.builders.PoolBuilder;
import org.bonitasoft.studio.model.process.builders.TaskBuilder;
import org.bonitasoft.studio.pagedesigner.core.FormContextApplicationFinder;
import org.bonitasoft.studio.pagedesigner.core.utils.RestletTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;

@RunWith(MockitoJUnitRunner.class)
public class FormContextResourceTest extends RestletTest {

    @Spy
    private FormContextResource formContextResource;
    @Spy
    private FormContextApplicationFinder formContextFinder;

    private static final String FORMID = "myFormId";

    @Before
    public void setup() {
        final List<AbstractProcess> processes = new ArrayList<AbstractProcess>();
        doReturn(processes).when(formContextResource).getAllProcesses();
        doReturn(FORMID).when(formContextResource).getAttribute(FormContextResource.ATTR_FORMID);
        doReturn(formContextResource).when(formContextFinder).create(any(Request.class), any(Response.class));
        doReturn(formContextResource).when(formContextFinder).create(any(Class.class), any(Request.class), any(Response.class));
        doReturn(formContextResource).when(formContextFinder).find(any(Request.class), any(Response.class));
    }

    @Override
    public FormContextApplicationFinder getFormContextFinder() {
        return formContextFinder;
    }

    @Test
    public void testGetFormContext_OfNotMappedFormThrowException() throws Exception {
        doReturn(new ArrayList<AbstractProcess>()).when(formContextResource).getAllProcesses();
        final Response response = request("/workspace/form/plop/context").get();

        assertThat(response.getStatus()).isEqualTo(Status.CLIENT_ERROR_NOT_FOUND);
        assertThat(response.getEntityAsText()).contains("No mapping found for the form with id " + FORMID);
    }

    @Test
    public void testGetFormContextForFormMappedOnPoolOverview() {
        final List<AbstractProcess> processes = new ArrayList<AbstractProcess>();
        final AbstractProcess pool = PoolBuilder.aPool()
                .havingOverviewFormMapping(FormMappingBuilder.aFormMapping()
                        .havingTargetForm(ExpressionBuilder.aConstantExpression().withContent(FORMID)))
                .havingData(BusinessObjectDataBuilder.createBusinessObjectDataBuilder().withName("businessObjectName").withClassname("EClassNameSample"))
                .havingData(JavaObjectDataBuilder.aData().havingDataType(JavaDataTypeBuilder.create().withName("java.util.List")))
                .build();
        processes.add(pool);
        pool.getOverviewFormMapping().setTargetForm(ExpressionHelper.createFormReferenceExpression("useless", FORMID));
        doReturn(processes).when(formContextResource).getAllProcesses();

        final Response response = request("/workspace/form/" + FORMID + "/context").get();
        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
        assertThat(response.getEntityAsText()).isEqualTo("{\"businessObjectName\":{\"className\":\"EClassNameSample\",\"type\":\"BusinessData\"}}");
    }

    @Test
    public void testGetFormContextForFormMappedOnPoolInstanciation() {
        final List<AbstractProcess> processes = new ArrayList<AbstractProcess>();
        final AbstractProcess pool = PoolBuilder.aPool()
                .havingFormMapping(FormMappingBuilder.aFormMapping()
                        .havingTargetForm(ExpressionBuilder.aConstantExpression().withContent(FORMID)))
                .havingData(BusinessObjectDataBuilder.createBusinessObjectDataBuilder().withName("businessObjectName"))
                .build();
        processes.add(pool);
        pool.getFormMapping().setTargetForm(ExpressionHelper.createFormReferenceExpression("useless", FORMID));
        doReturn(processes).when(formContextResource).getAllProcesses();

        final Response response = request("/workspace/form/" + FORMID + "/context").get();
        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
        assertThat(response.getEntityAsText()).isEqualTo("{}");
    }

    @Test
    public void testGetFormContextForFormMappedOnTask() {
        final List<AbstractProcess> processes = new ArrayList<AbstractProcess>();
        final AbstractProcess pool = PoolBuilder.aPool()
                .havingElements(TaskBuilder.aTask()
                        .havingFormMapping(FormMappingBuilder.aFormMapping()))
                .havingData(BusinessObjectDataBuilder.createBusinessObjectDataBuilder().withName("businessObjectName"))
                .build();
        processes.add(pool);
        ((PageFlow) pool.getElements().get(0)).getFormMapping().setTargetForm(ExpressionHelper.createFormReferenceExpression("useless", FORMID));
        doReturn(processes).when(formContextResource).getAllProcesses();

        final Response response = request("/workspace/form/" + FORMID + "/context").get();
        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
        assertThat(response.getEntityAsText()).isEqualTo("{\"businessObjectName\":{\"type\":\"BusinessData\"}}");
    }

    @Test
    public void testGetFormContextWithSeveralBusinessData() {
        final List<AbstractProcess> processes = new ArrayList<AbstractProcess>();
        final AbstractProcess pool = PoolBuilder.aPool()
                .havingOverviewFormMapping(FormMappingBuilder.aFormMapping()
                        .havingTargetForm(ExpressionBuilder.aConstantExpression().withContent(FORMID)))
                .havingData(BusinessObjectDataBuilder.createBusinessObjectDataBuilder().withName("businessObjectName1").withClassname("EClassNameSample"))
                .havingData(JavaObjectDataBuilder.aData().havingDataType(JavaDataTypeBuilder.create().withName("java.util.List")))
                .havingData(BusinessObjectDataBuilder.createBusinessObjectDataBuilder().withName("businessObjectName2").withClassname("EClassNameSample"))
                .build();
        processes.add(pool);
        pool.getOverviewFormMapping().setTargetForm(ExpressionHelper.createFormReferenceExpression("useless", FORMID));
        doReturn(processes).when(formContextResource).getAllProcesses();

        final Response response = request("/workspace/form/" + FORMID + "/context").get();
        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
        assertThat(response.getEntityAsText())
                .isEqualTo(
                        "{\"businessObjectName1\":{\"className\":\"EClassNameSample\",\"type\":\"BusinessData\"},\"businessObjectName2\":{\"className\":\"EClassNameSample\",\"type\":\"BusinessData\"}}");
    }

}
