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

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.NullProgressMonitor;

import io.sidespin.eclipse.jre.discovery.core.IManagedVMDetector;
import io.sidespin.eclipse.jre.discovery.core.IManagedVMWatcher;
import io.sidespin.eclipse.jre.discovery.core.ManagedVM;
import io.sidespin.eclipse.jre.discovery.core.internal.ManagedVMEvent.ManagedVMEventKind;

public class VMWatcherService implements IManagedVMWatcher {

	private boolean active;
	private Path directory;
	private WatchService watchService;
	private WatchKey key;
	private IManagedVMDetector detector;

	private ListenerList<ManagedVMEventListener> eventListeners = new ListenerList<>();

	public VMWatcherService(IManagedVMDetector detector) {
		this.detector = detector;
		this.directory = detector.getRootDirectory().toPath();
	}

	public void start() throws IOException, InterruptedException {
		if (active) {
			System.err.println("File watcher already started on " + directory);
		}
		if (!Files.isDirectory(directory)) {
			System.err.println("Cannot watch " + directory + " as it is not a directory!");
			return;
		}
		System.err.println("Start watching " + directory);
		active = true;

		var watcher = new Thread(() -> {
			try {
				watchService = FileSystems.getDefault().newWatchService();
				key = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
						// StandardWatchEventKinds.ENTRY_MODIFY,
						StandardWatchEventKinds.ENTRY_DELETE);
				while (active && (key = watchService.take()) != null) {
					for (WatchEvent<?> event : key.pollEvents()) {
						String spath = event.context().toString();
						System.err.println(event.kind() + " " + spath);
						File file = directory.resolve(spath).toFile();
						if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind()) && file.isDirectory()) {
							var vms = detector.detectVMs(new NullProgressMonitor());
							for (var managedVM : vms) {
								emitVMAddedEvent(managedVM);
							}
						} else if (StandardWatchEventKinds.ENTRY_DELETE.equals(event.kind())) {
							emitVMDeletedEvent(file);
						}
					}
					key.reset();
				}
			} catch (ClosedWatchServiceException e) {
				System.err.println("watchService was closed!");
				// Already stopped
				active = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				stop();
			}
			System.err.println("Filewatcher stopped");
		}, "Java Runtime Watcher - " + directory);
		watcher.setDaemon(true);
		watcher.start();
	}

	private void emitVMAddedEvent(ManagedVM mvm) {
		var event = new ManagedVMEvent(ManagedVMEventKind.VM_DELETED, mvm);
		for (var listener : eventListeners) {
			try {
				listener.managedVMAdded(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void emitVMDeletedEvent(File directory) {
		var event = new ManagedVMEvent(ManagedVMEventKind.VM_DELETED, new ManagedVM(directory));
		for (var listener : eventListeners) {
			try {
				listener.managedVMDeleted(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addListener(ManagedVMEventListener listener) {
		if (listener != null) {
			eventListeners.add(listener);
		}
	}

	@Override
	public void removeListener(ManagedVMEventListener listener) {
		if (listener != null) {
			eventListeners.remove(listener);
		}
	}

	@Override
	public void stop() {
		active = false;
		System.err.println("Stop watching " + directory);
		if (key != null) {
			key.cancel();
			key = null;
		}
		if (watchService != null) {
			try {
				watchService.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isWatching() {
		return active;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}

}
