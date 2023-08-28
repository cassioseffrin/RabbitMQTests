package br.edu.seffrin.senderReceiver;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Scheduler {

	public static void main(String[] args) {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		Runnable conn = () -> {
			try {
				registranoRMQ();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};

		ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(conn, 0, 5, TimeUnit.SECONDS);

		future.cancel(true);

		future = scheduler.scheduleAtFixedRate(conn, 0, 8, TimeUnit.SECONDS);

		// Sleep for a while to allow the task to run
		try {
			Thread.sleep(20000); // Let the task run for 20 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		future.cancel(true);
		scheduler.shutdown();
	}

	public static void registranoRMQ() {
		System.out.println("Task executed at " + new Date());
	}
}

//public class Scheduler {
//	public static void main(String[] args) {
//		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//		// Schedule a task to run every 5 seconds
//		ScheduledFuture<?> future = scheduler.scheduleAtFixedRate( yourMethodToRun, 0, 5, TimeUnit.SECONDS);
//		
//		
//		// Cancel all scheduled tasks
//		future.cancel(true);
//		
//		
//	    future = scheduler.scheduleAtFixedRate(Scheduler::yourMethodToRun, 0, 7, TimeUnit.SECONDS);
//
//		// Shutdown the scheduler
//
//		// Sleep for a while to allow the task to run
//		try {
//			Thread.sleep(20000); // Let the task run for 20 seconds
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		scheduler.shutdown();
//	}
//
//	public   void yourMethodToRun() {
//		System.out.println("Task executed at " + new Date());
//	}
//}
