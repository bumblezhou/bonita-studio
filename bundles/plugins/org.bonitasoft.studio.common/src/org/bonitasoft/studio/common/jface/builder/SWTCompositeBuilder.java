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

import org.bonitasoft.studio.common.jface.builder.layout.GridLayoutBuilder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SWTCompositeBuilder extends SWTControlBuilder<SWTCompositeBuilder> {

    private final Composite composite;

    public SWTCompositeBuilder(final Composite composite) {
        super(composite);
        this.composite = composite;
    }

    public GridLayoutBuilder applyGridLayout() {
        return GridLayoutBuilder.applyGridLayout(composite);
    }

    public SWTLabelBuilder aLabel(final String text) {
        return new SWTLabelBuilder(new Label(composite, SWT.NONE));
    }

    public SWTTextBuilder aText() {
        return new SWTTextBuilder(new Text(composite, SWT.BORDER));
    }

    public SWTTextBuilder aText(final int style) {
        return new SWTTextBuilder(new Text(composite, style));
    }

    public SWTTextBuilder aText(final String id) {
        return aText().withId(id);
    }
}
