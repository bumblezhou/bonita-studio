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
package org.bonitasoft.studio.common.jface.builder.databinding;

import static com.google.common.base.Preconditions.checkState;

import org.bonitasoft.studio.common.jface.builder.SWTControlBuilder;
import org.bonitasoft.studio.common.jface.databinding.UpdateValueStrategyFactory;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;

public class ValueDatabindingBuilder {

    protected IObservableValue modelObservableValue;
    protected IObservableValue targetObservableValue;
    private UpdateValueStrategy targetToModel;
    private UpdateValueStrategy modelToTarget;
    private final SWTControlBuilder<?> contorlBuilder;

    private <C extends SWTControlBuilder<?>> ValueDatabindingBuilder(final C contorlBuilder) {
        this.contorlBuilder = contorlBuilder;
    }

    public static <C extends SWTControlBuilder<?>> ValueDatabindingBuilder addBinding(final C contorlBuilder) {
        return new ValueDatabindingBuilder(contorlBuilder);
    }

    public ValueDatabindingBuilder modelObservableValue(final IObservableValue modelObservableValue) {
        this.modelObservableValue = modelObservableValue;
        return this;
    }

    public ValueDatabindingBuilder targetObservableValue(final IObservableValue targetObservableValue) {
        this.targetObservableValue = targetObservableValue;
        return this;
    }

    public ValueDatabindingBuilder targetStrategy(final UpdateValueStrategyFactory targetUpdateValueStrategy) {
        targetToModel = targetUpdateValueStrategy.create();
        return this;
    }

    public ValueDatabindingBuilder modelStrategy(final UpdateValueStrategyFactory modelUpdateValueStrategyFactory) {
        modelToTarget = modelUpdateValueStrategyFactory.create();
        return this;
    }

    public <C extends SWTControlBuilder<?>> C bind(final DataBindingContext context) {
        checkState(targetObservableValue != null);
        checkState(modelObservableValue != null);
        context.bindValue(targetObservableValue, modelObservableValue, targetToModel, modelToTarget);
        return (C) contorlBuilder;
    }

    public ValueDatabindingBuilder observeText() {
        targetObservableValue = SWTObservables.observeText(contorlBuilder.getControl(), SWT.Modify);
        return this;
    }

    public ValueDatabindingBuilder observeVisible() {
        targetObservableValue = SWTObservables.observeVisible(contorlBuilder.getControl());
        return this;
    }

}
