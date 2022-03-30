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

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.InstanceScope;

import io.sidespin.eclipse.jre.discovery.core.IManagedVMDetector;
import io.sidespin.eclipse.jre.discovery.core.IManagedVMWatcher;
import io.sidespin.eclipse.jre.discovery.core.ManagedVM;
import io.sidespin.eclipse.jre.discovery.core.internal.preferences.JREDiscoveryPreferences;

/**
 * @author Fred Bricon
 *
 */
public class VMDetectorManager implements IPreferenceChangeListener {

	private Set<IManagedVMDetector> detectors = new LinkedHashSet<>();
	private boolean watching;
	private ManagedVMService vmService;
	private boolean initialized;

	/**
	 * @param vmService
	 */
	public VMDetectorManager(ManagedVMService vmService) {
		Assert.isNotNull(vmService);
		this.vmService = vmService;
	}

	public synchronized void initialize() {
		if (!initialized) {
			var dd = ExtensionsReader.readVMDetectorExtensions();
			dd.forEach((id, d) -> {
				File dir = new File(d.getDirectory().replace("\\", File.separator).replace("/", File.separator)
						.replace("~", System.getProperty("user.home")));
				DefaultManagedVMDetector detector = new DefaultManagedVMDetector(dir, "[" + d.getLabel() + "]",
						d.isEnabledByDefault(), d.isWatchingByDefault());
				detectors.add(detector);
				var optWatcher = detector.getWatcher();
				if (optWatcher.isPresent()) {
					optWatcher.get().addListener(vmService);
				}
			});
		}
	}

	public Collection<ManagedVM> detectVMs(IProgressMonitor monitor) {
		if (detectors == null || detectors.isEmpty()) {
			return Collections.emptyList();
		}
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		SubMonitor sub = SubMonitor.convert(monitor, detectors.size());
		Set<ManagedVM> managedVMs = new LinkedHashSet<>();
		for (IManagedVMDetector detector : detectors) {
			if (sub.isCanceled()) {
				return managedVMs;
			}
			managedVMs.addAll(detector.detectVMs(sub.split(1)));
		}
		return managedVMs;
	}

	public synchronized void addManagedVMDetector(IManagedVMDetector detector) {
		if (detectors == null) {
			detectors = new LinkedHashSet<>();
		}
		detectors.add(detector);
		if (detector.getWatcher().isEmpty()) {
			return;
		}
		var watcher = detector.getWatcher().get();
		// watcher.addListener(vmService);
		if (watching && detector.isEnabled()) {
			try {
				watcher.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void startWatchingVMs() {
		watching = true;
		if (detectors == null || detectors.isEmpty()) {
			return;
		}
		detectors.stream().filter(d -> d.isWatchingEnabled())
				.map(d -> d.getWatcher())
				.filter(Optional::isPresent)
				.map(Optional::get)
				.forEach(this::startWatcher);
	}

	public void stopWatchingVMs() {
		watching = false;
		if (detectors == null || detectors.isEmpty()) {
			return;
		}
		detectors.stream().map(d -> d.getWatcher())
				.filter(Optional::isPresent)
				.map(Optional::get)
				.forEach(this::stopWatcher);
	}

	private void startWatcher(IManagedVMWatcher w) {
		try {
			w.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void stopWatcher(IManagedVMWatcher w) {
		try {
			w.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent event) {
		if (JREDiscoveryPreferences.WATCH_JRE_DIRECTORIES_KEY.equals(event.getKey())) {
			if (Boolean.parseBoolean(String.valueOf(event.getNewValue()))) {
				startWatchingVMs();
			} else {
				stopWatchingVMs();
			}
		}
	}

}
