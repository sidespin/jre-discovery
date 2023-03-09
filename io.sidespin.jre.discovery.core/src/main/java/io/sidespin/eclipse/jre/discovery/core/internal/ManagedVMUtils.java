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

import org.eclipse.jdt.launching.AbstractVMInstall;
import org.eclipse.jdt.launching.IVMInstall;

/**
 * @author Fred Bricon
 *
 */
public class ManagedVMUtils {

	private ManagedVMUtils() {
	}

	public static boolean isManagedVM(IVMInstall vm) {
		if (vm instanceof AbstractVMInstall) {
			AbstractVMInstall svm = (AbstractVMInstall) vm;
			return svm.getAttribute("managedVM") != null;
		}
		return false;
	}
	
	public static boolean wasJustDiscovered(IVMInstall vm) {
		if (vm instanceof AbstractVMInstall) {
			AbstractVMInstall svm = (AbstractVMInstall) vm;
			return ManagedVMService.SESSION_ID.equals(svm.getAttribute("sessionId"));
		}
		return false;
	}

}
