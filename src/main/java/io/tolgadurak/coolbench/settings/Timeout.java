package io.tolgadurak.coolbench.settings;

import java.util.concurrent.TimeUnit;

import io.tolgadurak.coolbench.algorithm.BenchmarkAlgorithm;
import io.tolgadurak.coolbench.algorithm.SHA512;

public class Timeout {
	private int value;
	private TimeUnit unit;

	private Timeout(int value, TimeUnit unit) {
		this.value = value;
		this.unit = unit;
	}

	public static Timeout getInstance(int value, TimeUnit unit) {
		return new Timeout(value, unit);
	}

	public static Timeout getInstance(BenchmarkAlgorithm algorithm) {
		if(algorithm instanceof SHA512) {
			return new Timeout(1, TimeUnit.HOURS);
		}
		throw new IllegalArgumentException("Benchmark algorithm cannot be found");
	}
	
	public int getValue() {
		return value;
	}

	public TimeUnit getUnit() {
		return unit;
	}

}
