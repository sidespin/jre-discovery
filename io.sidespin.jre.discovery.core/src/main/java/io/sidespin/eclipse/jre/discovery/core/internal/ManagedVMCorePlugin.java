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
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.framework.BundleContext;

import io.sidespin.eclipse.jre.discovery.core.internal.preferences.JREDiscoveryPreferences;

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
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(Constants.PLUGIN_ID);
		prefs.addPreferenceChangeListener(vmDetectorManager);		
		vmDetectorManager.initialize();
		boolean isWatchingEnabled = prefs.getBoolean(JREDiscoveryPreferences.WATCH_JRE_DIRECTORIES_KEY, true);
		if (isWatchingEnabled) {
			vmDetectorManager.startWatchingVMs();
		}
		updateVMJob = new UpdateVMsJob(vmService, vmDetectorManager);

		boolean isAutomaticDiscoveryEnabled = prefs.getBoolean(JREDiscoveryPreferences.ENABLED_ON_STARTUP_KEY, true);
		if (isAutomaticDiscoveryEnabled) {
			updateVMJob.schedule(1000);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (updateVMJob != null) {
			updateVMJob.cancel();
		}
		if (vmDetectorManager != null) {
			InstanceScope.INSTANCE.getNode(Constants.PLUGIN_ID).removePreferenceChangeListener(vmDetectorManager);
			vmDetectorManager.stopWatchingVMs();
			vmDetectorManager = null;
		}
		vmService = null;
		super.stop(context);
	}

	public static String getUniqueIdentifier() {
		return Constants.PLUGIN_ID; //$NON-NLS-1$
	}
}