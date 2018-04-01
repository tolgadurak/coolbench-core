package io.tolgadurak.coolbench.calculator;

public class ScoreCalculator {
	private static final int MAX_SCORE = 10000000; // Max Score is reached when benchmark is completed in 1ms.

	public int doAction(long durationInMillis) {
		return (int) (MAX_SCORE / durationInMillis);
	}
}
