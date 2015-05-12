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

import org.bonitasoft.studio.common.jface.builder.SWTCompositeBuilder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;

public class GridDataBuilder {

    private final GridData gridData;
    private final Control control;

    private GridDataBuilder(final GridData gridData, final Control control) {
        this.gridData = gridData;
        this.control = control;
    }

    public static GridDataBuilder applyGridData(final Control control) {
        final GridData gridData = new GridData();
        control.setLayoutData(gridData);
        return new GridDataBuilder(gridData, control);
    }

    public GridDataBuilder withCenterCollapse() {
        gridData.horizontalAlignment = SWT.CENTER;
        gridData.verticalAlignment = SWT.CENTER;
        gridData.grabExcessHorizontalSpace = false;
        gridData.grabExcessVerticalSpace = false;
        return this;
    }

    public GridDataBuilder grabExcessHorizontalSpace() {
        gridData.grabExcessHorizontalSpace = true;
        return this;
    }

    public GridDataBuilder grabExcessVerticalSpace() {
        gridData.grabExcessVerticalSpace = true;
        return this;
    }

    public GridDataBuilder horizontalSpan(final int horizontalSpan) {
        gridData.horizontalSpan = horizontalSpan;
        return this;
    }

    public GridDataBuilder verticalSpan(final int verticalSpan) {
        gridData.verticalSpan = verticalSpan;
        return this;
    }

    public GridDataBuilder minimumHeight(final int minimumHeight) {
        gridData.minimumHeight = minimumHeight;
        return this;
    }

    public GridDataBuilder minimumWidth(final int minimumWidth) {
        gridData.minimumWidth = minimumWidth;
        return this;
    }

    public GridDataBuilder verticalIndent(final int verticalIndent) {
        gridData.verticalIndent = verticalIndent;
        return this;
    }

    public GridDataBuilder horizontalIndent(final int horizontalIndent) {
        gridData.horizontalIndent = horizontalIndent;
        return this;
    }

    public GridDataBuilder heightHint(final int heightHint) {
        gridData.heightHint = heightHint;
        return this;
    }

    public GridDataBuilder widthHint(final int widthHint) {
        gridData.widthHint = widthHint;
        return this;
    }

    public GridDataBuilder fillVertical() {
        gridData.verticalAlignment = SWT.FILL;
        return this;
    }

    public GridDataBuilder alignMiddle() {
        gridData.verticalAlignment = SWT.CENTER;
        return this;
    }

    public GridDataBuilder alignTop() {
        gridData.verticalAlignment = SWT.TOP;
        return this;
    }

    public GridDataBuilder alignBottom() {
        gridData.verticalAlignment = SWT.BOTTOM;
        return this;
    }

    public GridDataBuilder fillHorizontal() {
        gridData.horizontalAlignment = SWT.FILL;
        return this;
    }

    public GridDataBuilder alignLeft() {
        gridData.horizontalAlignment = SWT.LEFT;
        return this;
    }

    public GridDataBuilder alignRight() {
        gridData.horizontalAlignment = SWT.RIGHT;
        return this;
    }

    public GridDataBuilder alignCenter() {
        gridData.horizontalAlignment = SWT.CENTER;
        return this;
    }

    public GridDataBuilder exclude() {
        gridData.exclude = true;
        return this;
    }

    public SWTCompositeBuilder followedBy() {
        return new SWTCompositeBuilder(control.getParent());
    }

}
