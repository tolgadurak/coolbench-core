package io.tolgadurak.coolbench.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.tolgadurak.coolbench.settings.Timeout;
import io.tolgadurak.coolbench.task.SHA512Task;

public class SHA512 implements BenchmarkAlgorithm {

	private static final Logger logger = LogManager.getLogger(SHA512.class);
	/**
	 * The SHA-512 hash algorithm defined in the FIPS PUB 180-2.
	 */
	private static final String SHA_512 = "SHA-512";
	private Queue<Future<?>> futureQueue = new ConcurrentLinkedQueue<>();

	@Override
	public boolean run(int taskCount, int nThreads, Timeout timeout) throws InterruptedException, TimeoutException {
		if(taskCount % nThreads != 0) {
			throw new IllegalArgumentException("Task count must be real product of threads");
		}
		boolean doneWithinTimeout = false;
		timeout = setTimeout(timeout);
		ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
		int hashCountPerTask = taskCount / nThreads;
		logger.debug("Task count: " + taskCount);
		logger.debug("Task count per thread: " + hashCountPerTask);
		submitThreads(executorService, nThreads, hashCountPerTask);
		executorService.shutdown();
		try {
			doneWithinTimeout = executorService.awaitTermination(timeout.getValue(), timeout.getUnit());
			if (!doneWithinTimeout) {
				cancelAllTasks();
				executorService.shutdownNow(); // Force all executing tasks to stop
				throw new TimeoutException();
			}
		} catch (InterruptedException e) {
			logger.error("Error while performing benchmark with " + nThreads + " threads", e);
			throw e;
		}
		return doneWithinTimeout;
	}

	private void cancelAllTasks() {
		while (!futureQueue.isEmpty()) {
			Future<?> future = futureQueue.poll();
			future.cancel(true);
		}
	}

	private Timeout setTimeout(Timeout timeout) {
		if (timeout == null) {
			timeout = Timeout.getInstance(this);
		}
		return timeout;
	}

	private void submitThreads(ExecutorService executorService, int nThreads, int hashCountPerTask) {
		for (int i = 0; i < nThreads; i++) {
			SHA512Task task = new SHA512Task(hashCountPerTask);
			Future<?> future = executorService.submit(task);
			futureQueue.add(future);
		}
	}

	public byte[] hash(int iterationCount) {
		byte[] hash = new byte[8];
		try {
			MessageDigest md = MessageDigest.getInstance(SHA_512);
			for (int i = 0; i < iterationCount; i++) {
				if (Thread.interrupted()) {
					logger.debug("Hashing task has been cancelled. Calculated hash: " + i);
					return hash;
				}
				md.reset();
				hash = md.digest(hash);
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error while initializing message digest");
		}
		logger.debug("Hashing task has been completed succesfully");
		return hash;
	}

}
