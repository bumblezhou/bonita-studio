/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.studio.diagram.custom;

import org.bonitasoft.studio.common.extension.IConfigurationIdProvider;
import org.bonitasoft.studio.model.process.MainProcess;

public class ConfigurationIdProvider implements IConfigurationIdProvider {

	@Override
	public Object getConfigurationId(MainProcess diagram) {
		return get(diagram);
	}

	@Override
	public boolean isConfigurationIdValid(MainProcess diagram) {
		Object configurationId = diagram.getConfigId();
		return configurationId != null && configurationId.toString().equals(get(diagram));
	}

	protected String get(MainProcess diagram) {
		return diagram.getName() + diagram.getBonitaModelVersion() + diagram.getBonitaVersion();
	}

}
