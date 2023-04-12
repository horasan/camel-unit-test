package com.horasan.helper;

import java.util.ArrayList;
import java.util.List;

import com.horasan.model.GetPeopleResponse;
import com.horasan.model.StarWarsPerson;

public class TestHelper {
	public static GetPeopleResponse getMockEmptyGetPeopleResponse() {
		GetPeopleResponse mockGetPeopleResponse = new GetPeopleResponse();
		
		List<StarWarsPerson> allOfThemFromMockPeopleApi = new ArrayList<StarWarsPerson>();
		mockGetPeopleResponse.setCount(0);
		mockGetPeopleResponse.setNext("mockNextPageURL");
		mockGetPeopleResponse.setPrevious("mockPreviousPageURL");
		mockGetPeopleResponse.setResults(allOfThemFromMockPeopleApi);
		return mockGetPeopleResponse;
	}
	
	public static GetPeopleResponse getMockGetPeopleResponseWith2People() {
		
		List<StarWarsPerson> allOfThemFromMockPeopleApi = getStarWarsPeopleListWith2People();
		
		GetPeopleResponse mockGetPeopleResponse = new GetPeopleResponse();
		mockGetPeopleResponse.setCount(2);
		mockGetPeopleResponse.setNext("mockNextPageURL");
		mockGetPeopleResponse.setPrevious("mockPreviousPageURL");
		mockGetPeopleResponse.setResults(allOfThemFromMockPeopleApi);
		return mockGetPeopleResponse;
	}

	public static List<StarWarsPerson> getStarWarsPeopleListWith2People() {
		List<StarWarsPerson> allOfThemFromMockPeopleApi = new ArrayList<>();
		
		StarWarsPerson p0 = new StarWarsPerson();
		p0.setName("fromMockPeopleApi_name0");
		
		StarWarsPerson p1 = new StarWarsPerson();
		p1.setName("fromMockPeopleApi_name1");
		
		allOfThemFromMockPeopleApi.add(p0);
		allOfThemFromMockPeopleApi.add(p1);
		return allOfThemFromMockPeopleApi;
	}
	
	
}
