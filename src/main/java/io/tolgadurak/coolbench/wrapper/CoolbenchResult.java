package io.tolgadurak.coolbench.wrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CoolbenchResult {
	private int score;
	private int nTimes;
	private int nThreads;
	private String algorithmName;
	private String javaVersion;

	public int getScore() {
		return score;
	}

	public int getnTimes() {
		return nTimes;
	}

	public int getnThreads() {
		return nThreads;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public String toJson() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
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
		this.javaVersion = System.getProperty("java.version");
		this.algorithmName = builder.algorithmName;
	}

	public static class Builder {
		private int score;
		private int nTimes;
		private int nThreads;
		private String algorithmName;

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

		public Builder algorithmName(String algorithmName) {
			this.algorithmName = algorithmName;
			return this;
		}

		public CoolbenchResult build() {
			return new CoolbenchResult(this);
		}
	}
}
