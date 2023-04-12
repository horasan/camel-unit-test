package com.horasan.batch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.horasan.routes.CamelBaseConfiguration;

@Component
public class StarWarsOperationsBatch extends CamelBaseConfiguration {

	public static String ROUTE_ID = "1_route_timerSWBatch";
	public static String ID_Processor_GetStarWarsPeople = "ID_Processpor_GetStarWarsPeople";
	
	@Autowired
	private FluentProducerTemplate fluentProducerTemplate;
	
	@Override
	public void specificCamelConfigurationForThisRoute() throws Exception {
		from("timer://swBatch?period=10000") // each 10 seconds.
		.routeId(StarWarsOperationsBatch.ROUTE_ID)
		.process(exchange -> {
			Exchange result = fluentProducerTemplate.to("direct:GetStarWarsPeople").request(Exchange.class);
		}).id(StarWarsOperationsBatch.ID_Processor_GetStarWarsPeople)
		.process(exchange -> {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			System.out.println("Results are processed!" + " " + dtf.format(now));
		});
	}	
}
