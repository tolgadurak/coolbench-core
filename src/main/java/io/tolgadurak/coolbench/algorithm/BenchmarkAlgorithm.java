package io.tolgadurak.coolbench.algorithm;

import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;

import io.tolgadurak.coolbench.settings.Timeout;

public interface BenchmarkAlgorithm {

	/**
	 * Runs dedicated benchmark algorithm
	 * 
	 * @param nTimes
	 *            the count that specifies how many times algorithm to be executed
	 * @param nThreads
	 *            n new threads to perform n times execution of algorithm in parallel
	 * @param timeout
	 *            it guarantees the execution of the tasks to be sync with current
	 *            thread. If timeout elapsed, throw TimeoutExeption. In other words,
	 *            while tasks are executed, it blocks current thread up to timeout
	 * @return true if execution is finished within timeout and false if the timeout
	 *         elapsed before termination
	 * @throws TimeoutException
	 *             this method throws if timeout elapsed during execution
	 */
	boolean run(int nTimes, int nThreads, Timeout timeout) throws InterruptedException, TimeoutException;

	/**
	 * Runs dedicated benchmark algorithm asynchronously
	 * 
	 * @param nTimes
	 *            the count that specifies how many times algorithm to be executed
	 * @param nThreads
	 *            n new threads to perform n times execution of algorithm in parallel
	 */
	void runAsync(int nTimes, int nThreads) throws InterruptedException;

	/**
	 * Returns algorithm name of the instance
	 * @return algorithm name
	 */
	String getName();

	public static BenchmarkAlgorithm getInstance(String algorithm) {
		if (StringUtils.equals("SHA-512", algorithm)) {
			return new SHA512();
		}
		throw new IllegalArgumentException("Algorithm cannot be found");
	}
}
