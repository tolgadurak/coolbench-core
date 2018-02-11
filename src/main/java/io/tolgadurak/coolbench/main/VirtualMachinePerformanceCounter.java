package io.tolgadurak.coolbench.main;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.tolgadurak.coolbench.algorithm.BenchmarkAlgorithm;
import io.tolgadurak.coolbench.settings.Timeout;

public class VirtualMachinePerformanceCounter {

	private static final Logger logger = LogManager.getLogger(VirtualMachinePerformanceCounter.class);

	public static void main(String[] args) throws InterruptedException {
		// Epic: Implement multithreaded hash mechanism in order to benchmark all cores
		// on CPU

		logger.info("Benchmark has been started");
		// This will perform single core hashing task

		VirtualMachinePerformanceCounter counter = new VirtualMachinePerformanceCounter();
		counter.benchmark();

	}

	public void benchmark() throws InterruptedException {
		int taskCount = 64;
		int nThreads = 8;
		BenchmarkAlgorithm benchmarkAlgorithm = BenchmarkAlgorithm.getInstance("SHA-512");

		long startTime = System.nanoTime();
		boolean done = false;
		try {
			done = benchmarkAlgorithm.run(taskCount, nThreads, Timeout.getInstance(1, TimeUnit.HOURS));
		} catch (TimeoutException e) {
			logger.error("Timeout is exceeded while performing benchmark");
		}	
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);

		logger.info("duration - " + duration / 1000000 + "ms");
		logger.info("Done within timeout: " + done);
	}
}
