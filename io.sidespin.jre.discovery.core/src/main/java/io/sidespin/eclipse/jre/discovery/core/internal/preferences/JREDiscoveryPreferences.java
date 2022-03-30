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
package io.sidespin.eclipse.jre.discovery.core.internal.preferences;

import io.sidespin.eclipse.jre.discovery.core.internal.Constants;

public class JREDiscoveryPreferences {

  public static final String ENABLED_ON_STARTUP_KEY = Constants.PLUGIN_ID + ".enabledOnStartup";

  public static final String WATCH_JRE_DIRECTORIES_KEY = Constants.PLUGIN_ID + ".watchJREDirectories";

  private static final JREDiscoveryPreferences INSTANCE = new JREDiscoveryPreferences();

  private JREDiscoveryPreferences() {
  }

  public static JREDiscoveryPreferences getInstance() {
    return INSTANCE;
  }

}