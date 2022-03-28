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

/**
 * @author Fred Bricon
 *
 */
public interface ManagedVMEventListener {

	void managedVMAdded(ManagedVMEvent event);

	void managedVMDeleted(ManagedVMEvent event);

}
