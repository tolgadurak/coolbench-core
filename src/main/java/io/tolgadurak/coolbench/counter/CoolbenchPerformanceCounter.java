package io.tolgadurak.coolbench.counter;

import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.tolgadurak.coolbench.algorithm.BenchmarkAlgorithm;
import io.tolgadurak.coolbench.calculator.ScoreCalculator;
import io.tolgadurak.coolbench.settings.Timeout;
import io.tolgadurak.coolbench.wrapper.CoolbenchResult;

public class CoolbenchPerformanceCounter {

	private static final Logger logger = LogManager.getLogger(CoolbenchPerformanceCounter.class);

	public CoolbenchResult benchmark(int nTimes, int nThreads, BenchmarkAlgorithm benchmarkAlgorithm)
			throws InterruptedException {
		boolean done = true;
		long startTime = System.nanoTime();
		Timeout timeout = Timeout.getInstance(benchmarkAlgorithm);
		ScoreCalculator scoreCalculator = new ScoreCalculator();
		logger.debug("Benchmark has been started");
		try {
			benchmarkAlgorithm.run(nTimes, nThreads, timeout);
		} catch (TimeoutException e) {
			logger.error("Timeout is exceeded while performing benchmark");
			done = false;
		}
		logger.debug("Benchmark has been ended");
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		long durationInMillis = duration / 1000000;
		logger.debug("duration - " + durationInMillis + "ms");
		logger.debug("Done within timeout: " + done);
		int score = scoreCalculator.doAction(durationInMillis);
		return buildCoolbenchResult(score, nTimes, nThreads, benchmarkAlgorithm);
	}

	private CoolbenchResult buildCoolbenchResult(int score, int nTimes, int nThreads, BenchmarkAlgorithm algorithm) {
		return new CoolbenchResult.Builder().score(score).nTimes(nTimes).nThreads(nThreads).algorithm(algorithm)
				.build();
	}
}
