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

import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;

public class RequestBuilder {

    private final String uri;

    public RequestBuilder(final String uri) {
        this.uri = uri;
    }

    public Response get() {
        final Client client = new Client(Protocol.HTTP);
        return client.handle(new Request(Method.GET, uri));
    }

    public Response post(final String value) {
        final Client client = new Client(Protocol.HTTP);
        final Request request = new Request(Method.POST, uri);
        request.setEntity(value, MediaType.APPLICATION_JSON);
        return client.handle(request);
    }

    public Response put(final String value) {
        final Client client = new Client(Protocol.HTTP);
        final Request request = new Request(Method.PUT, uri);
        request.setEntity(value, MediaType.APPLICATION_JSON);
        return client.handle(request);
    }
}