package com.horasan.model;

import java.util.List;

public class GetPeopleResponse {

	private Integer count;
	private String next;
	private String previous;
	private List<StarWarsPerson> results;
	
	public GetPeopleResponse() {}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public List<StarWarsPerson> getResults() {
		return results;
	}

	public void setResults(List<StarWarsPerson> results) {
		this.results = results;
	}
	
	
	
}
