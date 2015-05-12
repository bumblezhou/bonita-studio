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
package org.bonitasoft.studio.swt.finder;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory;

public class ControlFinder {

    private final Widget parent;
    private final org.eclipse.swtbot.swt.finder.finders.ControlFinder swtControlFinder;

    public ControlFinder(final Widget parent) {
        this.parent = parent;
        swtControlFinder = new org.eclipse.swtbot.swt.finder.finders.ControlFinder();
    }

    public static ControlFinder aConrolFinder(final Widget parent) {
        return new ControlFinder(parent);
    }

    public Text findTextById(final String id) {
        final List<Text> findControls = swtControlFinder.findControls(parent, WidgetMatcherFactory.<Text> withId(id), true);
        checkState(findControls.size() == 1);
        return findControls.get(0);
    }
}