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
package org.bonitasoft.studio.common.jface.builder.layout;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

public class GridLayoutBuilder {

    private final GridLayout gridLayout;

    private GridLayoutBuilder(final GridLayout gridLayout) {
        this.gridLayout = gridLayout;
    }

    public static GridLayoutBuilder applyGridLayout(final Composite composite) {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        composite.setLayout(gridLayout);
        return new GridLayoutBuilder(gridLayout);
    }

    public static GridLayoutBuilder onGridLayout(final Composite composite) {
        final Layout layout = composite.getLayout();
        if (layout instanceof GridLayout) {
            return new GridLayoutBuilder((GridLayout) layout);
        }
        throw new IllegalStateException("Composite has to have a GridLayout. Has " + layout);
    }

    public GridLayoutBuilder numColumns(final int numColumns) {
        gridLayout.numColumns = numColumns;
        return this;
    }

    public GridLayoutBuilder columnsEqualWidth() {
        gridLayout.makeColumnsEqualWidth = true;
        return this;
    }

    public GridLayoutBuilder horizontalSpacing(final int horizontalSpacing) {
        gridLayout.horizontalSpacing = horizontalSpacing;
        return this;
    }

    public GridLayoutBuilder verticalSpacing(final int verticalSpacing) {
        gridLayout.verticalSpacing = verticalSpacing;
        return this;
    }

    public GridLayoutBuilder marginWidth(final int marginWidth) {
        gridLayout.marginWidth = marginWidth;
        return this;
    }

    public GridLayoutBuilder marginHeight(final int marginHeight) {
        gridLayout.marginHeight = marginHeight;
        return this;
    }

    public GridLayoutBuilder marginTop(final int marginTop) {
        gridLayout.marginTop = marginTop;
        return this;
    }

    public GridLayoutBuilder marginBottom(final int marginBottom) {
        gridLayout.marginBottom = marginBottom;
        return this;
    }

    public GridLayoutBuilder marginLeft(final int marginLeft) {
        gridLayout.marginLeft = marginLeft;
        return this;
    }

    public GridLayoutBuilder marginRight(final int marginRight) {
        gridLayout.marginRight = marginRight;
        return this;
    }
}
