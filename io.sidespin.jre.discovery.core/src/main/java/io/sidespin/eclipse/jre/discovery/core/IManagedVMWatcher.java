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

import java.io.IOException;

import io.sidespin.eclipse.jre.discovery.core.internal.ManagedVMEventListener;

/**
 * @author Fred Bricon
 *
 */
public interface IManagedVMWatcher {

	void start() throws IOException, InterruptedException;

	void stop();

	boolean isWatching();

	boolean isEnabled();

	void removeListener(ManagedVMEventListener listener);

	void addListener(ManagedVMEventListener listener);

}
