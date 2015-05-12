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
package org.bonitasoft.studio.common.jface.builder;

import org.bonitasoft.studio.common.jface.SWTBotConstants;
import org.bonitasoft.studio.common.jface.builder.layout.GridDataBuilder;
import org.eclipse.swt.widgets.Control;

public abstract class SWTControlBuilder<B extends SWTControlBuilder<?>> {

    private final Control control;

    public SWTControlBuilder(final Control control) {
        this.control = control;
    }

    public B withId(final String id) {
        control.setData(SWTBotConstants.SWTBOT_WIDGET_ID_KEY, id);
        return (B) this;
    }

    public SWTCompositeBuilder followedBy() {
        return new SWTCompositeBuilder(control.getParent());
    }

    public Control getControl() {
        return control;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.studio.common.jface.builder.layout.GridDataBuilder#exclude()
     */
    public GridDataBuilder applyGridData() {
        return GridDataBuilder.applyGridData(control);
    }

}
