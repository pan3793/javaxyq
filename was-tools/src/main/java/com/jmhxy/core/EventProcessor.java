package com.jmhxy.core;

import com.jmhxy.comps.GDialog;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @deprecated
 */
public class EventProcessor {
	private Thread processingThread;
	private LinkedBlockingQueue<JXYEvent> eventQueue;
	private GameCanvas gameCanvas;

	public EventProcessor() {
		this.eventQueue = new LinkedBlockingQueue();
		initThread();
		this.gameCanvas = GameMain.getGameCanvas();
	}

	private void initThread() {
		this.processingThread = new Thread() {
			public void run() {
				JXYEvent event = null;
				GDialog dlg = null;
				try {
					for (;;) {
						event = (JXYEvent) EventProcessor.this.eventQueue.poll(1000L, TimeUnit.MILLISECONDS);
						if (event != null) {
							switch (event.id) {
							case 256:
								break;
							case 264:
								break;
							case 272:
								break;
							}

							if (dlg != null)
								if (dlg.getParent() == EventProcessor.this.gameCanvas) {
									EventProcessor.this.gameCanvas.remove(dlg);
								} else {
									EventProcessor.this.gameCanvas.add(dlg);
									EventProcessor.this.gameCanvas.setComponentZOrder(dlg, 0);
								}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		this.processingThread.setDaemon(true);
		this.processingThread.start();
	}

	public void put(JXYEvent event) {
		try {
			this.eventQueue.put(event);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

/*
 * Location: D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\core\EventProcessor.class
 * Java compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */