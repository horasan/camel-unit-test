package com.horasan.routes;

import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.horasan.model.StarWarsPerson;
import com.horasan.services.StarWarsPeopleService;

@Component
public class SaveStarWarsPersonRoute extends CamelBaseConfiguration {

	public static String ROUTE_ID = "4_route_SaveStarWarsPersonRoute";
	
	public static String ID_processor_for_savePerson_operation = "processor_for_savePerson_operation";
	
	@Autowired
	private StarWarsPeopleService peopleServiceImpl;
	
	@Override
	public void specificCamelConfigurationForThisRoute() throws Exception {
		from("direct:SaveStarWarsPerson")
		.routeId(SaveStarWarsPersonRoute.ROUTE_ID)
		.process(savePerson()).id(SaveStarWarsPersonRoute.ID_processor_for_savePerson_operation)
		;
	}
	
	private Processor savePerson() {
		return exchange -> {
			
			StarWarsPerson starWarsPerson = exchange.getMessage().getBody(StarWarsPerson.class);
			peopleServiceImpl.saveStarWarsPeople(starWarsPerson);
			
		};
	}

}
