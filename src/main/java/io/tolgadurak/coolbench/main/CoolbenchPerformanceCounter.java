package io.tolgadurak.coolbench.main;

import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.tolgadurak.coolbench.algorithm.BenchmarkAlgorithm;
import io.tolgadurak.coolbench.calculator.ScoreCalculator;
import io.tolgadurak.coolbench.settings.Timeout;

public class CoolbenchPerformanceCounter {

	private static final Logger logger = LogManager.getLogger(CoolbenchPerformanceCounter.class);

	public static void main(String[] args) throws InterruptedException {
		CoolbenchPerformanceCounter counter = new CoolbenchPerformanceCounter();
		BenchmarkAlgorithm algorithm = BenchmarkAlgorithm.getInstance("SHA-512");
		// This will perform multi core hashing task
		long score = counter.benchmark(33554432, 8, algorithm);
		logger.info("Performance score: " + score);
		
	}

	public long benchmark(int nTimes, int nTasks, BenchmarkAlgorithm benchmarkAlgorithm) throws InterruptedException {
		boolean done = false;
		long startTime = System.nanoTime();
		Timeout timeout = Timeout.getInstance(benchmarkAlgorithm);
		ScoreCalculator scoreCalculator = new ScoreCalculator();
		logger.debug("Benchmark has been started");
		try {
			benchmarkAlgorithm.run(nTimes, nTasks, timeout);
		} catch (TimeoutException e) {
			logger.error("Timeout is exceeded while performing benchmark");
		}
		logger.debug("Benchmark has been ended");
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		long durationInMillis = duration / 1000000;
		logger.info("duration - " + durationInMillis + "ms");
		logger.info("Done within timeout: " + done);
		return scoreCalculator.doAction(durationInMillis);
	}
}
