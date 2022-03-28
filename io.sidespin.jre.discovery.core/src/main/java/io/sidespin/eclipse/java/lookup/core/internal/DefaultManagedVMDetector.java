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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.osgi.util.NLS;

import io.sidespin.eclipse.jre.discovery.core.IManagedVMDetector;
import io.sidespin.eclipse.jre.discovery.core.IManagedVMWatcher;
import io.sidespin.eclipse.jre.discovery.core.ManagedVM;

public class DefaultManagedVMDetector implements IManagedVMDetector {

	private File rootDirectory;
	private String prefix;
	private boolean isEnabled;
	private boolean isWatchingEnabled;
	private Optional<IManagedVMWatcher> watcher;

	public DefaultManagedVMDetector(File directory, String prefix, boolean enabled, boolean isWatching) {
		this.rootDirectory = directory;
		this.prefix = prefix;
		this.isEnabled = enabled;
		this.isWatchingEnabled = isWatching;
	}

	public Collection<ManagedVM> detectVMs(IProgressMonitor monitor) {
		var dir = getRootDirectory();
		if (dir == null || !dir.isDirectory()) {
			return Collections.emptySet();
		}
		System.err.println("Detecting VMs in " + dir);

		LinkedHashSet<ManagedVM> results = new LinkedHashSet<>();
		Set<File> ignore = new HashSet<>();
		searchDir(dir, results, ignore, monitor);
		return results;
	}

	protected void searchDir(File directory, Set<ManagedVM> results, Set<File> ignore, IProgressMonitor monitor) {
		if (monitor.isCanceled()) {
			return;
		}
		var curDirVM = getManagedVM(directory, ignore, monitor);
		if (curDirVM != null) {
			results.add(curDirVM);
			return;
		}
		String[] names = directory.list();
		if (names == null) {
			return;
		}
		List<File> subDirs = new ArrayList<>();
		for (String name : names) {
			if (monitor.isCanceled()) {
				return;
			}
			File file = new File(directory, name);
			if (!file.isDirectory()) {
				continue;
			}
			try {
				String log = NLS.bind("Found {0} - Searching {1}", new String[] { Integer.toString(results.size()),
						file.getCanonicalPath().replaceAll("&", "&&") });
				monitor.subTask(log); // @see bug 29855 //$NON-NLS-1$ //$NON-NLS-2$
			} catch (IOException e) {
			}
			curDirVM = getManagedVM(file, ignore, monitor);
			if (curDirVM == null) {
				// Didn't find a VM here, we'll track it down further
				subDirs.add(file);
			} else {
				results.add(curDirVM);
			}
		}
		while (!subDirs.isEmpty()) {
			File subDir = subDirs.remove(0);
			searchDir(subDir, results, ignore, monitor);
			if (monitor.isCanceled()) {
				return;
			}
		}

	}

	protected ManagedVM getManagedVM(File file, Set<File> ignore, IProgressMonitor monitor) {
		if (file.isDirectory() && !ignore.contains(file)) {
			IVMInstallType[] vmTypes = JavaRuntime.getVMInstallTypes();

			// Take the first VM install type that claims the location as a
			// valid VM install. VM install types should be smart enough to not
			// claim another type's VM, but just in case...
			for (IVMInstallType type : vmTypes) {
				if (monitor.isCanceled()) {
					return null;
				}
				IStatus status = type.validateInstallLocation(file);
				if (status.isOK()) {
					String filePath = file.getPath();
					int index = filePath.lastIndexOf(File.separatorChar);
					File newFile = file;
					// remove bin folder from install location as java executables are found only
					// under bin for Java 9 and above
					if (index > 0 && filePath.substring(index + 1).equals("bin")) { //$NON-NLS-1$
						newFile = new File(filePath.substring(0, index));
					}
					return new ManagedVM(newFile, type, prefix);
				}
			}
		}
		return null;
	}

	@Override
	public File getRootDirectory() {
		return rootDirectory;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public boolean isWatchingEnabled() {
		return isWatchingEnabled;
	}

	@Override
	public Optional<IManagedVMWatcher> getWatcher() {
		if (watcher != null) {
			return watcher;
		}
		watcher = isWatchingEnabled ? Optional.of(new VMWatcherService(this)) : Optional.empty();
		return watcher;
	}

}
