package com.horasan.routes;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.horasan.model.StarWarsPerson;

@Component
public class FormatStarWarsPeopleRoute extends CamelBaseConfiguration {

	public static String ROUTE_ID_FormatStarWarsPeopleRoute = "3_route_FormatStarWarsPeopleRoute";
	
	@Override
	public void specificCamelConfigurationForThisRoute() throws Exception {

		from("direct:FormatStarWarsPeople")
		.routeId(ROUTE_ID_FormatStarWarsPeopleRoute)
		.process(formatPeople())
		;
		
	}
	
	@SuppressWarnings("unchecked")
	private Processor formatPeople() {
		return exchange -> {
			
			List<StarWarsPerson> people = exchange.getMessage().getBody(List.class);
			
			List<StarWarsPerson> modifiedPeople = people.stream().map(
						p -> {
							p.setName(p.getName().toUpperCase() + "-M");
							return p;
						}).collect(Collectors.toList());
			
					
			exchange.getMessage().setBody(modifiedPeople);
			
		};
	}

}
