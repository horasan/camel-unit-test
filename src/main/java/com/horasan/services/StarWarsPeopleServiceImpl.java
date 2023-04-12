package com.horasan.services;

import org.springframework.stereotype.Component;

import com.horasan.model.StarWarsPerson;

@Component
public class StarWarsPeopleServiceImpl implements StarWarsPeopleService {

	@Override
	public StarWarsPerson saveStarWarsPeople(StarWarsPerson swPeople) {
		return swPeople;
	}
	
}
