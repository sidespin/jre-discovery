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
 *     fbricon - initial API and implementation
 *******************************************************************************/

package io.sidespin.eclipse.jre.discovery.core.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

import io.sidespin.eclipse.jre.discovery.core.internal.Constants;

/**
 * @author Fred Bricon
 *
 */
public class JREDiscoveryPreferenceInitializer extends AbstractPreferenceInitializer {

	  @Override
	  public void initializeDefaultPreferences() {
	    IEclipsePreferences store = InstanceScope.INSTANCE.getNode(Constants.PLUGIN_ID);
	    store.putBoolean(JREDiscoveryPreferences.ENABLED_ON_STARTUP_KEY, true);
	    store.putBoolean(JREDiscoveryPreferences.WATCH_JRE_DIRECTORIES_KEY, true);
	    store.putBoolean(JREDiscoveryPreferences.NOTIFICATIONS_ENABLED_KEY, true);
	  }
}