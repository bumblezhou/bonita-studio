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
package org.bonitasoft.studio.contract.ui.property.input;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.studio.model.process.ProcessFactory;
import org.bonitasoft.studio.model.process.ProcessPackage;
import org.bonitasoft.studio.swt.AbstractSWTTestCase;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Romain Bioteau
 *
 */
public class ContractInputObservableFactoryTest extends AbstractSWTTestCase {

    private ContractInputObservableFactory contractInputObservableFactory;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        createDisplayAndRealm();
        contractInputObservableFactory = new ContractInputObservableFactory();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        dispose();
    }

    @Test
    public void should_createObservable_return_contract_input_observable_list() throws Exception {
        final WritableValue contractObservable = new WritableValue(Realm.getDefault());
        contractObservable.setValue(ProcessFactory.eINSTANCE.createContract());
        final IObservable observable = contractInputObservableFactory.createObservable(contractObservable);
        assertThat(observable).isInstanceOf(IObservableList.class);
        assertThat(((IObservableList) observable).getElementType()).isEqualTo(ProcessPackage.Literals.CONTRACT__INPUTS);
    }

    @Test
    public void should_createObservable_return_contract_input_inputs_observable_list() throws Exception {
        final IObservable observable = contractInputObservableFactory.createObservable(ProcessFactory.eINSTANCE.createContractInput());
        assertThat(observable).isInstanceOf(IObservableList.class);
        assertThat(((IObservableList) observable).getElementType()).isEqualTo(ProcessPackage.Literals.CONTRACT_INPUT__INPUTS);
    }

    @Test
    public void should_createObservable_return_null_otherwise() throws Exception {
        final IObservable observable = contractInputObservableFactory.createObservable(null);
        assertThat(observable).isNull();
    }
}
