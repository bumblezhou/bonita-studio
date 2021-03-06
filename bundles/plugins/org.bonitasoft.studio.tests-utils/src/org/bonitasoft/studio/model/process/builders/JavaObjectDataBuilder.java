/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.studio.model.process.builders;

import org.bonitasoft.studio.model.process.JavaObjectData;
import org.bonitasoft.studio.model.process.ProcessFactory;

/**
 * @author Romain Bioteau
 *
 */
public class JavaObjectDataBuilder<T extends JavaObjectData, B extends JavaObjectDataBuilder<T, B>> extends DataBuilder<T, B> {

    public static <B extends JavaObjectDataBuilder<JavaObjectData, B>> JavaObjectDataBuilder<JavaObjectData, B> createJavaObjectDataBuilder() {
        return new JavaObjectDataBuilder<JavaObjectData, B>();
    }

    public B withClassname(final String classname) {
        getBuiltInstance().setClassName(classname);
        return getThis();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T newInstance() {
        return (T) ProcessFactory.eINSTANCE.createJavaObjectData();
    }
}
