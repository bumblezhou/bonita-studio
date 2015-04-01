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
package org.bonitasoft.studio.pagedesigner.core;

import org.bonitasoft.studio.common.log.BonitaStudioLog;
import org.bonitasoft.studio.pagedesigner.PageDesignerPlugin;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;

public class WorkspaceResourceServerManager {

    private Component component;
    private Server server;
    private static WorkspaceResourceServerManager INSTANCE;

    public synchronized static WorkspaceResourceServerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WorkspaceResourceServerManager();
        }
        return INSTANCE;
    }

    private WorkspaceResourceServerManager() {

    }

    public synchronized void start(final int port) throws Exception {
        if (component == null || !component.isStarted()) {
            component = new Component();
            server = component.getServers().add(Protocol.HTTP, port);
            component.getDefaultHost().attach("/api", new WorkspaceApplication(new FormContextApplicationFinder()));

            BonitaStudioLog.debug("Starting RESTLET server on port " + port + "...", PageDesignerPlugin.PLUGIN_ID);
            component.start();
        }
    }

    public synchronized void stop() throws Exception {
        if (component != null && component.isStarted()) {
            BonitaStudioLog.debug("Stopping RESTLET server...", PageDesignerPlugin.PLUGIN_ID);

            component.stop();
            component = null;
            server = null;
        }
    }

    protected Component getComponent() {
        return component;
    }

    public int runningPort() {
        if (server == null || server.isStopped()) {
            throw new IllegalStateException("Restlet server is not running");
        }
        return server.getPort();
    }

    public boolean isRunning() {
        return component != null && component.isStarted();
    }

}
