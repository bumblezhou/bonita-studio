/**
 * Copyright (C) 2015 Bonitasoft S.A.
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
package org.bonitasoft.studio.pagedesigner.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.net.URL;
import java.util.Locale;

import org.bonitasoft.studio.preferences.BonitaPreferenceConstants;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Romain Bioteau
 */
@RunWith(MockitoJUnitRunner.class)
public class PageDesignerURLFactoryTest implements BonitaPreferenceConstants {

    private PageDesignerURLFactory pageDesignerURLBuilder;
    @Mock
    private IEclipsePreferences preferenceStore;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        doReturn("localhost").when(preferenceStore).get(CONSOLE_HOST, DEFAULT_HOST);
        doReturn(8080).when(preferenceStore).getInt(CONSOLE_PORT, DEFAULT_PORT);
        pageDesignerURLBuilder = new PageDesignerURLFactory(preferenceStore);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void should_openPageDesignerHome_return_URL_pointing_to_page_builder_webapp() throws Exception {
        assertThat(pageDesignerURLBuilder.openPageDesignerHome()).isEqualTo(new URL("http://localhost:8080/designer"));
    }

    @Test
    public void should_openPage_return_URL_pointing_to_page_builder_webapp_on_the_given_page() throws Exception {
        assertThat(pageDesignerURLBuilder.openPage("page-id")).isEqualTo(
                new URL("http://localhost:8080/designer/#/" + Locale.getDefault().getLanguage() + "/pages/page-id"));
    }

    @Test
    public void should_newPage_return_URL_to_post_a_new_page() throws Exception {
        assertThat(pageDesignerURLBuilder.newPage()).isEqualTo(
                new URL("http://localhost:8080/designer/api/rest/pages/"));
    }

    @Test
    public void should_exportPage_return_URL_to_that_export_the_page_with_given_id() throws Exception {
        assertThat(pageDesignerURLBuilder.exportPage("my-page-id")).isEqualTo(
                new URL("http://localhost:8080/designer/api/export/page/my-page-id"));
    }

}
