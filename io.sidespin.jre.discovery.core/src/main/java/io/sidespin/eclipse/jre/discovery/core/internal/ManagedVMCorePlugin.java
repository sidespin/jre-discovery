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

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class ManagedVMCorePlugin extends Plugin {

	private static ManagedVMCorePlugin instance;

	private ManagedVMService vmService;

	private VMDetectorManager vmDetectorManager;

	private UpdateVMsJob updateVMJob;

	public ManagedVMCorePlugin() {
		super();
		Assert.isTrue(instance == null);
		instance = this;
	}

	public static ManagedVMCorePlugin getDefault() {
		return instance;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		vmService = new ManagedVMService();
		vmDetectorManager = new VMDetectorManager(vmService);
		vmDetectorManager.initialize();
		vmDetectorManager.startWatchingVMs();
		updateVMJob = new UpdateVMsJob(vmService, vmDetectorManager);
		updateVMJob.schedule(1000);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (updateVMJob != null) {
			updateVMJob.cancel();
		}
		if (vmDetectorManager != null) {
			vmDetectorManager.stopWatchingVMs();
			vmDetectorManager = null;
		}
		vmService = null;
		super.stop(context);
	}

	/*
	 * Convenience method which returns the unique identifier of this plug-in.
	 */
	public static String getUniqueIdentifier() {
		return "io.sidespin.jre.discovery.core"; //$NON-NLS-1$
	}
}