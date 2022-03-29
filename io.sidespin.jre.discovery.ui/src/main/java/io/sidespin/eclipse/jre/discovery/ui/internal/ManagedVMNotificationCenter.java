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
package io.sidespin.eclipse.jre.discovery.ui.internal;

import static io.sidespin.eclipse.jre.discovery.core.internal.ManagedVMUtils.isManagedVM;

import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallChangedListener;
import org.eclipse.jdt.launching.PropertyChangeEvent;

public class ManagedVMNotificationCenter implements IVMInstallChangedListener {

	private final ManagedVMNotificationAggregatorJob notificationJob;

	public ManagedVMNotificationCenter(ManagedVMNotificationAggregatorJob notificationJob) {
		this.notificationJob = notificationJob;
	}

	@Override
	public void defaultVMInstallChanged(IVMInstall previous, IVMInstall current) {
	}

	@Override
	public void vmChanged(PropertyChangeEvent event) {
	}

	@Override
	public void vmAdded(IVMInstall vm) {
		if (isManagedVM(vm)) {
			notificationJob.queue(vm);
		}
	}

	@Override
	public void vmRemoved(IVMInstall vm) {
	}

}
