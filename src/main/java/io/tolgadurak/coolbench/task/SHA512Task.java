package io.tolgadurak.coolbench.task;

import io.tolgadurak.coolbench.algorithm.SHA512;

public class SHA512Task extends SHA512 implements Runnable {
	private int hashCount;

	public SHA512Task(int hashCount) {
		this.hashCount = hashCount;
	}

	@Override
	public void run() {
		hash(hashCount);
	}

}
