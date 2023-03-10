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
package io.sidespin.eclipse.jre.discovery.ui.internal.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import io.sidespin.eclipse.jre.discovery.core.internal.preferences.JREDiscoveryPreferences;
import io.sidespin.eclipse.jre.discovery.ui.internal.ManagedVMUIActivator;

/**
 * @author Fred Bricon
 */
public class JREDiscoveryPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  public JREDiscoveryPreferencePage() {
    super();
    setPreferenceStore(ManagedVMUIActivator.getInstance().getPreferenceStore());
  }

  @Override
  public void init(IWorkbench workbench) {
	  setDescription("Automatically detect JREs managed by SDKMan, JBang, asdf or Jabba. Also detects installations in /usr/lib/jvm on Linux and Eclipse Adoptium installations on Windows.");
  }

  @Override
  protected void createFieldEditors() {
    BooleanFieldEditor detectOnStartupDiscovery = new BooleanFieldEditor(JREDiscoveryPreferences.ENABLED_ON_STARTUP_KEY,
        "Enable automatic JRE discovery on startup", getFieldEditorParent());
    addField(detectOnStartupDiscovery);

    BooleanFieldEditor watchDirectories = new BooleanFieldEditor(
    		JREDiscoveryPreferences.WATCH_JRE_DIRECTORIES_KEY, "Watch managed JRE directories",
        getFieldEditorParent());
    addField(watchDirectories);

    BooleanFieldEditor displayNotifications = new BooleanFieldEditor(
    		JREDiscoveryPreferences.NOTIFICATIONS_ENABLED_KEY, "Enable notifications when new JREs are detected",
        getFieldEditorParent());
    addField(displayNotifications);
  }

}