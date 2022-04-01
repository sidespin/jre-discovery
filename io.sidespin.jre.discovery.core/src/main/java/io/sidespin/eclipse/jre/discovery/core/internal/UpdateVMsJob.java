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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;

import io.sidespin.eclipse.jre.discovery.core.ManagedVM;

public class UpdateVMsJob extends Job {

	private ManagedVMService vmService;
	private VMDetectorManager detectorManager;

	public UpdateVMsJob(ManagedVMService vmService, VMDetectorManager detectorManager) {
		super("JDK detector");
		this.vmService = vmService;
		this.detectorManager = detectorManager;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		var vms = detectorManager.detectVMs(monitor);
		SubMonitor sub = SubMonitor.convert(monitor, vms.size());
		for (ManagedVM vm : vms) {
			try {
				if (sub.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				vmService.addVM(vm, sub.split(1));
				sub.worked(1);
			} catch (Exception e) {
				return Status.error("Failed to add " + vm+"!", e);
			}
		}
		return Status.OK_STATUS;
	}

}
