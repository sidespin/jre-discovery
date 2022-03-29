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

import static io.sidespin.eclipse.jre.discovery.core.internal.ManagedVMUtils.isManagedVM;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jface.notifications.AbstractNotificationPopup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class ManagedVMNotificationAggregatorJob extends Job {

	// private static final String JAVA_VM_PREFERENCES_PAGE_ID =
	// "org.eclipse.jdt.debug.ui.preferences.VMPreferencePage";

	private static final long DELAY = 1000;
	private final Set<IVMInstall> notifications = new LinkedHashSet<>();

	public ManagedVMNotificationAggregatorJob() {
		super("Managed VM notification aggregator");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if (monitor.isCanceled()) {
			notifications.clear();
			return Status.CANCEL_STATUS;
		}

		Set<IVMInstall> currentQueue;

		synchronized (notifications) {
			currentQueue = new LinkedHashSet<>(this.notifications);
			notifications.clear();
		}
		if (currentQueue.isEmpty()) {
			return Status.OK_STATUS;
		}

		if (Display.getDefault() != null) {
			Display.getDefault().asyncExec(() -> {
				NotificationPopUp notificationPopUp = new NotificationPopUp(currentQueue, Display.getDefault());
				notificationPopUp.open();
			});
		}

		synchronized (this.notifications) {
			if (!notifications.isEmpty() && !monitor.isCanceled()) {
				schedule();
			}
		}
		return Status.OK_STATUS;
	}

	public void queue(IVMInstall... vms) {
		if (vms == null || vms.length == 0) {
			return;
		}
		synchronized (notifications) {
			for (var vm : vms) {
				if (isManagedVM(vm)) {
					notifications.add(vm);
				}
			}
		}
		schedule(DELAY);
	}

	private class NotificationPopUp extends AbstractNotificationPopup {

		private Set<IVMInstall> vms;

		public NotificationPopUp(Set<IVMInstall> vms, Display display) {
			super(display);
			this.vms = vms;
		}

		@Override
		protected String getPopupShellTitle() {
			return "New Java VM" + ((vms.size() > 1) ? "s" : "") + " detected";
		}

		@Override
		protected void createContentArea(Composite parent) {
			Label label = new Label(parent, SWT.WRAP);
			if (vms.size() > 1) {
				label.setText(vms.size() + " new Java VMs were automatically added.");
			} else {
				var name = vms.iterator().next().getName();
				label.setText(name + " was automatically added.");
			}
			// label.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			// PreferencesUtil.createPreferenceDialogOn(Display.getDefault().getActiveShell(),
			// JAVA_VM_PREFERENCES_PAGE_ID, new String[] { JAVA_VM_PREFERENCES_PAGE_ID },
			// null).open();
			// }));
		}
	}
}
