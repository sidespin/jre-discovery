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
package io.sidespin.eclipse.jre.discovery.ui.internal;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import io.sidespin.eclipse.jre.discovery.core.internal.ManagedVMCorePlugin;

public class ManagedVMUIActivator implements BundleActivator {

  public static final String PLUGIN_ID = "io.sidespin.eclipse.jre.discovery.ui";
  private static ManagedVMUIActivator instance;
  private ScopedPreferenceStore preferenceStore;
  private ManagedVMNotificationCenter notificationCenter;
  private ManagedVMNotificationAggregatorJob notificationJob;

  @Override
  public void start(BundleContext context) throws Exception {
    System.err.println("Starting " + ManagedVMCorePlugin.getUniqueIdentifier());
    notificationJob = new ManagedVMNotificationAggregatorJob();
    notificationCenter = new ManagedVMNotificationCenter(notificationJob);
    // HACK: trigger VM Initialization before registering the
    // VMInstallChangedListener,
    // as loading saved VMs automatically fires a VMAdded event on startup, which we
    // don't care
    JavaRuntime.getDefaultVMInstall();

    JavaRuntime.addVMInstallChangedListener(notificationCenter);
    instance = this;
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    instance = null;
    if (notificationCenter != null) {
      JavaRuntime.removeVMInstallChangedListener(notificationCenter);
    }
    if (notificationJob != null) {
      notificationJob.cancel();
      notificationJob = null;
    }
  }

  public static ManagedVMUIActivator getInstance() {
    return instance;
  }

  public IPreferenceStore getPreferenceStore() {
    // Create the preference store lazily.
    if (preferenceStore == null) {
      preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, ManagedVMCorePlugin.getUniqueIdentifier());
    }
    return preferenceStore;
  }
}