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
import java.util.Objects;

import org.eclipse.jdt.launching.IVMInstallType;

public class ManagedVM {// could be a record? Needs Java 17+
	private File directory;

	private String prefix;

	private IVMInstallType type;

	public ManagedVM(File directory, IVMInstallType type, String prefix) {
		this(directory);
		this.type = type;
		this.prefix = prefix;
	}

	public ManagedVM(File directory) {
		this.directory = directory;
	}

	public File getDirectory() {
		return directory;
	}

	public IVMInstallType getType() {
		return type;
	}

	public String getPrefix() {
		return prefix;
	}

	@Override
	public String toString() {
		String typeId = type == null ? null : type.getId();
		return "ManagedVM [directory=" + directory + ", prefix=" + prefix + ", type=" + typeId + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(directory, prefix, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManagedVM other = (ManagedVM) obj;
		String otherTypeId = other.type == null ? null : other.type.getId();
		return Objects.equals(directory, other.directory) && Objects.equals(prefix, other.prefix)
				&& Objects.equals(type.getId(), otherTypeId);
	}

}