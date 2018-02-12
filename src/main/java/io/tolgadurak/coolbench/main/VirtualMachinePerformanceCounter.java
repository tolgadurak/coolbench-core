package io.tolgadurak.coolbench.main;

import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.tolgadurak.coolbench.algorithm.BenchmarkAlgorithm;
import io.tolgadurak.coolbench.settings.Timeout;

public class VirtualMachinePerformanceCounter {

	private static final Logger logger = LogManager.getLogger(VirtualMachinePerformanceCounter.class);

	public static void main(String[] args) throws InterruptedException {
		VirtualMachinePerformanceCounter counter = new VirtualMachinePerformanceCounter();
		BenchmarkAlgorithm algorithm = BenchmarkAlgorithm.getInstance("SHA-512");
		// This will perform multi core hashing task
		counter.benchmark(algorithm);
		// This will perform multi core hashing task asynchronously
		counter.benchmarkAsync(algorithm);
	}

	private void benchmarkAsync(BenchmarkAlgorithm benchmarkAlgorithm) throws InterruptedException {
		int hashCount = 33554432;
		int nTasks = 8;
		logger.debug("Benchmark has been started asynchronously");
		benchmarkAlgorithm.runAsync(hashCount, nTasks);
	}

	public void benchmark(BenchmarkAlgorithm benchmarkAlgorithm) throws InterruptedException {
		int hashCount = 33554432;
		int nTasks = 8;
		boolean done = false;
		long startTime = System.nanoTime();
		logger.debug("Benchmark has been started");
		try {
			benchmarkAlgorithm.run(hashCount, nTasks, Timeout.getInstance(benchmarkAlgorithm));
		} catch (TimeoutException e) {
			logger.error("Timeout is exceeded while performing benchmark");
		}
		logger.debug("Benchmark has been ended");
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		logger.info("duration - " + duration / 1000000 + "ms");
		logger.info("Done within timeout: " + done);
	}
}
