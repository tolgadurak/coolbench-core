package io.tolgadurak.coolbench.wrapper;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.tolgadurak.coolbench.algorithm.BenchmarkAlgorithm;

@JsonRootName(value = "coolbenchResult")
public class CoolbenchResult {
	private int score;
	private int nTimes;
	private int nThreads;
	private BenchmarkAlgorithm algorithm;

	public int getScore() {
		return score;
	}

	public int getnTimes() {
		return nTimes;
	}

	public int getnThreads() {
		return nThreads;
	}

	public BenchmarkAlgorithm getAlgorithm() {
		return algorithm;
	}

	public String toJson() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		return objectMapper.writeValueAsString(this);
	}

	public String toJsonPrettyPrint() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		return objectMapper.writeValueAsString(this);
	}

	private CoolbenchResult(Builder builder) {
		this.score = builder.score;
		this.nTimes = builder.nTimes;
		this.nThreads = builder.nThreads;
		this.algorithm = builder.algorithm;
	}

	public static class Builder {
		private int score;
		private int nTimes;
		private int nThreads;
		private BenchmarkAlgorithm algorithm;

		public Builder score(int score) {
			this.score = score;
			return this;
		}

		public Builder nTimes(int nTimes) {
			this.nTimes = nTimes;
			return this;
		}

		public Builder nThreads(int nThreads) {
			this.nThreads = nThreads;
			return this;
		}

		public Builder algorithm(BenchmarkAlgorithm algorithm) {
			this.algorithm = algorithm;
			return this;
		}

		public CoolbenchResult build() {
			return new CoolbenchResult(this);
		}
	}
}
