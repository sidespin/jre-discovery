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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * @author Fred Bricon
 *
 */
public class ExtensionsReader {

	public static final String EXTENSION_VM_DETECTORS = Constants.PLUGIN_ID + ".managedVMDetectorDefinition"; //$NON-NLS-1$

	private ExtensionsReader() {
	}

	public static Map<String, ManagedVMDetectorDefinition> readVMDetectorExtensions(String... bundles) {
		Map<String, ManagedVMDetectorDefinition> detectors = new HashMap<>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint detectorsExtensionPoint = registry.getExtensionPoint(EXTENSION_VM_DETECTORS);
		if (detectorsExtensionPoint != null) {
			for (IExtension extension : detectorsExtensionPoint.getExtensions()) {
				if (bundles != null && bundles.length > 0) {
					var contributor = extension.getContributor().getName();
					if (!Stream.of(bundles).anyMatch(b -> Objects.equals(b, contributor))) {
						continue;
					}
				}
				IConfigurationElement[] elements = extension.getConfigurationElements();
				for (IConfigurationElement element : elements) {
					if ("simpleDetector".equals(element.getName())) {
						var dd = new ManagedVMDetectorDefinition();
						dd.setId(element.getAttribute("id"));
						dd.setLabel(element.getAttribute("label"));
						var watchingByDefault = Boolean.valueOf(element.getAttribute("isWatchingByDefault"));
						dd.setWatchingByDefault(watchingByDefault);
						String directory = element.getAttribute("directory");
						dd.setDirectory(directory);
						if (detectors.containsKey(dd.getId())) {
							continue;
						}
						detectors.put(dd.getId(), dd);
					}
				}
			}
		}
		return detectors;
	}

}
