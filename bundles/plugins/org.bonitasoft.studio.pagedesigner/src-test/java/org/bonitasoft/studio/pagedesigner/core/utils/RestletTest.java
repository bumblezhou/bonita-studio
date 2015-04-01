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
package org.bonitasoft.studio.pagedesigner.core.utils;

import java.io.IOException;
import java.io.InputStream;

import org.bonitasoft.studio.common.platform.tools.PlatformUtil;
import org.bonitasoft.studio.pagedesigner.core.FormContextApplicationFinder;
import org.bonitasoft.studio.pagedesigner.core.WorkspaceApplication;
import org.junit.After;
import org.junit.Before;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;

/**
 * Restlet test base class.
 * <ul>
 * <li>Allow to configure tested resource via {@link #getFormContextFinder()}</li>
 * <li>Allow to configure tested app via {@link #configureApplication()}</li>
 * <li>Start restlet server bound on a random port before each tests (and stop it after)</li>
 * <li>Provide {@link #request(String)} method to perform http requests</li>
 * </ul>
 *
 * @author Colin Puy
 * @author Aurelien Pupier - inspired from Colin work in Server project
 */
public abstract class RestletTest {

    private Component component;
    private String baseUri;

    /**
     * Configure the {@link Application} under test.
     * Override this method to fully configure app under test launched by server
     */
    protected Application configureApplication() {
        final FormContextApplicationFinder formContextFinder = getFormContextFinder();
        if (formContextFinder != null) {
            return new WorkspaceApplication(formContextFinder);
        } else {
            return new WorkspaceApplication(new FormContextApplicationFinder());
        }
    }

    /**
     * Override this method to be able to mock the FormResource
     *
     * @return
     */
    protected FormContextApplicationFinder getFormContextFinder() {
        return null;
    }

    /**
     * Create a request builder with server baseUri as base path
     * @param path absolute path to map request on
     */
    protected RequestBuilder request(final String path) {
        return new RequestBuilder(baseUri + path);
    }

    /**
     * Read a file in current package
     */
    protected String readFile(final String fileName) throws IOException {
        final InputStream resourceAsStream = this.getClass().getResourceAsStream(fileName);
        return PlatformUtil.getFileContent(resourceAsStream);
    }

    @Before
    public void startServer() throws Exception {
        baseUri = start();
    }

    private String start() throws Exception {
        component = new Component();
        final Server server = component.getServers().add(Protocol.HTTP, 0);
        // server.getContext().getParameters().add("tracing", "true");
        final Application application = configureApplication();
        component.getDefaultHost().attach(application);
        component.start();
        return "http://localhost:" + server.getEphemeralPort();
    }

    @After
    public void stopServer() throws Exception {
        stop();
    }

    private void stop() throws Exception {
        if (component != null && component.isStarted()) {
            component.stop();
        }
        component = null;
    }

}