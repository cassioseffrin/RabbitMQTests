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

 
		try {
			Thread.sleep(20000); //  20 s
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		future.cancel(true);
		scheduler.shutdown();
	}

	public static void registranoRMQ() {
		System.out.println("EXECUTADO " + new Date());
	}
}

//public class Scheduler {
//	public static void main(String[] args) {
//		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
 
//		ScheduledFuture<?> future = scheduler.scheduleAtFixedRate( yourMethodToRun, 0, 5, TimeUnit.SECONDS);
//		
//		
 
//		future.cancel(true);
//		
//		
//	    future = scheduler.scheduleAtFixedRate(Scheduler::yourMethodToRun, 0, 7, TimeUnit.SECONDS);
//
 
//
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
//		System.out.println("EXECUTADO " + new Date());
//	}
//}
