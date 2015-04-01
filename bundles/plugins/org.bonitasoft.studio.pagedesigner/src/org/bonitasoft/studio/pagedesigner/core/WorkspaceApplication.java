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
package org.bonitasoft.studio.pagedesigner.core;

import org.bonitasoft.studio.pagedesigner.core.resources.form.FormContextResource;
import org.bonitasoft.studio.pagedesigner.core.resources.lock.WorkspaceServerResource;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * @author Romain Bioteau
 */
public class WorkspaceApplication extends Application {

    private final FormContextApplicationFinder finder;

    public WorkspaceApplication(final FormContextApplicationFinder finder) {
        this.finder = finder;
    }

    @Override
    public Restlet createInboundRoot() {
        final Router router = new Router(getContext());
        router.attach("/workspace/form/{" + FormContextResource.ATTR_FORMID + "}/context", finder);
        router.attach(
                "/workspace/{" + WorkspaceServerResource.FILEPATH_ATTRIBUTE + "}/{" + WorkspaceServerResource.ACTION_ATTRIBUTE + "}",
                WorkspaceServerResource.class);
        //router.attach("/workspace/{action}", WorkspaceServerResource.class); seems not handled
        return router;
    }

}
