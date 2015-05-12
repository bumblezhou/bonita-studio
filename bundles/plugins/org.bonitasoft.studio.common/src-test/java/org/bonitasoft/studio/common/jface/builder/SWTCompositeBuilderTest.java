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

import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.studio.swt.finder.ControlFinder.aConrolFinder;

import org.bonitasoft.studio.swt.rules.RealmWithDisplay;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.junit.Rule;
import org.junit.Test;

public class SWTCompositeBuilderTest {

    @Rule
    public RealmWithDisplay realm = new RealmWithDisplay();

    class Employee {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }
    }

    @Test
    public void testName() throws Exception {
        final Employee employee = new Employee();
        final DataBindingContext context = new DataBindingContext();
        final Composite parent = realm.createComposite();

        new SWTCompositeBuilder(parent).aLabel("Hello").applyGridData().alignRight().followedBy().aText("employeeNameText")
                .addBinding()
                .observeText()
                .modelObservableValue(PojoObservables.observeValue(employee, "name"))
                .bind(context);

        final Text text = aConrolFinder(parent).findTextById("employeeNameText");

        text.setText("Marcel");

        assertThat(employee.getName()).isEqualTo("Marcel");

    }
}
