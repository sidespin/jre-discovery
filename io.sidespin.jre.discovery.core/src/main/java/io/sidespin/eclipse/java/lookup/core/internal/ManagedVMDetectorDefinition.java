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

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Fred Bricon
 *
 */
public class ManagedVMDetectorDefinition {

	private String id;

	private int priority;

	private String label;

	private boolean isWatchingByDefault;

	private String[] supportedPlatforms;

	private boolean isEnabledByDefault;

	private String directory;

	@Override
	public String toString() {
		return id + "- [" + directory + "]";
	}

	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(supportedPlatforms);
		result = prime * result + Objects.hash(directory, id, isEnabledByDefault, isWatchingByDefault, label, priority);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManagedVMDetectorDefinition other = (ManagedVMDetectorDefinition) obj;
		return Objects.equals(directory, other.directory) && Objects.equals(id, other.id)
				&& isEnabledByDefault == other.isEnabledByDefault && isWatchingByDefault == other.isWatchingByDefault
				&& Objects.equals(label, other.label) && priority == other.priority
				&& Arrays.equals(supportedPlatforms, other.supportedPlatforms);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label == null ? id : label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the isWatchingByDefault
	 */
	public boolean isWatchingByDefault() {
		return isWatchingByDefault;
	}

	/**
	 * @param isWatchingByDefault the isWatchingByDefault to set
	 */
	public void setWatchingByDefault(boolean isWatchingByDefault) {
		this.isWatchingByDefault = isWatchingByDefault;
	}

	/**
	 * @return the supportedPlatforms
	 */
	public String[] getSupportedPlatforms() {
		return supportedPlatforms;
	}

	/**
	 * @param supportedPlatforms the supportedPlatforms to set
	 */
	public void setSupportedPlatforms(String[] supportedPlatforms) {
		this.supportedPlatforms = supportedPlatforms;
	}

	/**
	 * @return the isEnabledByDefault
	 */
	public boolean isEnabledByDefault() {
		return isEnabledByDefault;
	}

	/**
	 * @param isEnabledByDefault the isEnabledByDefault to set
	 */
	public void setEnabledByDefault(boolean isEnabledByDefault) {
		this.isEnabledByDefault = isEnabledByDefault;
	}

}
