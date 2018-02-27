package io.tolgadurak.coolbench.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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

	@Override
	public boolean run(int nTimes, int nThreads, Timeout timeout) throws InterruptedException, TimeoutException {
		return handleTasks(nTimes, nThreads, timeout, false);
	}

	private boolean handleTasks(int nTimes, int nThreads, Timeout timeout, boolean isAsync)
			throws TimeoutException, InterruptedException {
		if (nTimes % nThreads != 0) {
			throw new IllegalArgumentException("Times count must be absolute product of n threads");
		}
		boolean doneWithinTimeout = false;
		timeout = setTimeout(timeout);
		ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
		int hashCountPerThreads = nTimes / nThreads;
		logger.debug("Hash count: " + nTimes);
		logger.debug("Hash count per threads: " + hashCountPerThreads);
		submitThreads(executorService, nThreads, hashCountPerThreads);
		executorService.shutdown();
		if (!isAsync) {
			try {
				doneWithinTimeout = executorService.awaitTermination(timeout.getValue(), timeout.getUnit());
				if (!doneWithinTimeout) {
					executorService.shutdownNow(); // Force all executing tasks to stop
					throw new TimeoutException();
				}
			} catch (InterruptedException e) {
				logger.error("Error while performing benchmark with " + nThreads + " threads", e);
				throw e;
			}
		}
		return doneWithinTimeout;
	}

	private Timeout setTimeout(Timeout timeout) {
		if (timeout == null) {
			timeout = Timeout.getInstance(this);
		}
		return timeout;
	}

	private void submitThreads(ExecutorService executorService, int nThreads, int hashCountPerThreads) {
		for (int i = 0; i < nThreads; i++) {
			SHA512Task thread = new SHA512Task(hashCountPerThreads);
			executorService.submit(thread);
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

	@Override
	public void runAsync(int nTimes, int nThreads) throws InterruptedException {
		try {
			handleTasks(nTimes, nThreads, Timeout.getInstance(0, TimeUnit.NANOSECONDS), true);
		} catch (InterruptedException e) {
			logger.error("Error while performing benchmark with " + nThreads + " threads", e);
			throw e;
		} catch (TimeoutException e) {
			logger.debug("Async algorithm is being run");
		}
	}

}
