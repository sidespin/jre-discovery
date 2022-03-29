/*******************************************************************************
 * Copyright (c) 2022 JRE Lookup project contributors.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Fred Bricon - initial API and implementation
 *******************************************************************************/

package io.sidespin.eclipse.jre.discovery.core.internal;

import io.sidespin.eclipse.jre.discovery.core.ManagedVM;

/**
 * @author Fred Bricon
 *
 */
public class ManagedVMEvent {

	private ManagedVM managedVM;
	private ManagedVMEventKind kind;

	enum ManagedVMEventKind {
		VM_ADDED, VM_DELETED
	}

	public ManagedVMEvent(ManagedVMEventKind kind, ManagedVM mvm) {
		this.managedVM = mvm;
		this.kind = kind;

	}

	public ManagedVM getManagedVM() {
		return managedVM;
	}

	public ManagedVMEventKind getKind() {
		return kind;
	}
}
