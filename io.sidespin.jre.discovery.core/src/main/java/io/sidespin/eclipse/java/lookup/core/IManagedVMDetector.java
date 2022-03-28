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

package io.sidespin.eclipse.jre.discovery.core;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Fred Bricon
 *
 */
public interface IManagedVMDetector {

	File getRootDirectory();

	boolean isEnabled();

	public Collection<ManagedVM> detectVMs(IProgressMonitor monitor);

	default Optional<IManagedVMWatcher> getWatcher() {
		return Optional.empty();
	}

	default boolean isWatchingEnabled() {
		return isEnabled() && getWatcher().isPresent();
	}

}
