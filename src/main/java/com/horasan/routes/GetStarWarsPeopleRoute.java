package com.horasan.routes;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.horasan.model.GetPeopleResponse;
import com.horasan.model.StarWarsPerson;


@Component
public class GetStarWarsPeopleRoute extends CamelBaseConfiguration {

	public static String ROUTE_ID = "2_route_GetStarWarsPeople";
	
	public static String ID_FOR_direct_FormatStarWarsPeople = "direct_FormatStarWarsPeople_in_GetStarWarsPeopleRoute";
	
	@Value("${starwars.api.url.people}")
	private String starWarsApiUrlForPeople;
	
	@Override
	public void specificCamelConfigurationForThisRoute() throws Exception {

		from("direct:GetStarWarsPeople")
		.routeId(GetStarWarsPeopleRoute.ROUTE_ID)
		.process(prepareGetRequest())
		.to(starWarsApiUrlForPeople)
		.process(prepareGetPeopleResponse())
		.to("direct:FormatStarWarsPeople").id(GetStarWarsPeopleRoute.ID_FOR_direct_FormatStarWarsPeople)
		.split(body())
		.to("direct:SaveStarWarsPerson") //save each item in the list separately.
		;
		
	}
	
	private Processor prepareGetPeopleResponse() {
		return exchange -> {

			ObjectMapper mapper = new ObjectMapper(); 
			
			GetPeopleResponse getPeopleResponse = mapper.readValue(exchange.getMessage().getBody(String.class), new TypeReference<GetPeopleResponse>(){});  

			List<StarWarsPerson> people = getPeopleResponse.getResults();
			exchange.getMessage().setBody(people);
			
		};
	}

	private Processor prepareGetRequest() {
		return exchange -> {
			
			exchange.getMessage().setHeader(Exchange.HTTP_METHOD, HttpMethod.GET.name());
			exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			
		};
	}

}
