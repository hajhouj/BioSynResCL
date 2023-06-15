package com.hajhouj.biosynres.cl;

public class Result {
	private int index;
	private String term;
	private int distance;

	public Result(int index, String term, int distance) {
		super();
		this.index = index;
		this.term = term;
		this.distance = distance;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

}
