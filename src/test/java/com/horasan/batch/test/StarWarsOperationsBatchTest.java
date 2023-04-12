package com.horasan.batch.test;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.horasan.batch.StarWarsOperationsBatch;

@CamelSpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@UseAdviceWith
@MockEndpoints
public class StarWarsOperationsBatchTest {
	
	@Autowired
	private FluentProducerTemplate fluentProducerTemplate;
	
	@Autowired
	private ProducerTemplate producerTemplate;
	
	@Autowired
	private CamelContext camelContext;
	
	@EndpointInject("mock:mockEndPointFor_StarWarsOperationsBatch_route_timerSWBatch")
	protected MockEndpoint mockEndPointFor_StarWarsOperationsBatch_route_timerSWBatch;
	
	@EndpointInject("mock:mockEndPointFor_direct_GetStarWarsPeople")
	protected MockEndpoint mockEndPointFor_direct_GetStarWarsPeople;
	
	/*
		Camel related items:
			AdviceWith.adviceWith
			AdviceWith.replaceFromWith
			AdviceWithRouteBuilder.weaveById
			ProducerTemplate
		Description:
			"from("timer://swBatch?period=10000")" is route is replaced with "direct:mockEndPointFor_StarWarsOperationsBatch_route_timerSWBatch" using AdviceWith.replaceFromWith (in StarWarsOperationsBatch.java).
			Processor for calling "direct:GetStarWarsPeople" is mocked with mockEndPointFor_direct_GetStarWarsPeople.
			
	 */
	@Test
	public void test_1_StarWarsOperationsBatch() throws Exception {

		AdviceWith.adviceWith(camelContext, StarWarsOperationsBatch.ROUTE_ID, routeBuilder -> {
			
			//"from("timer://swBatch?period=10000")" route is replaced with "direct:mockEndPointFor_StarWarsOperationsBatch_route_timerSWBatch".
			routeBuilder.replaceFromWith("direct:mockEndPointFor_StarWarsOperationsBatch_route_timerSWBatch");
			
			//Processor for calling "direct:GetStarWarsPeople" is mocked with mockEndPointFor_direct_GetStarWarsPeople. 
			routeBuilder.weaveById(StarWarsOperationsBatch.ID_Processor_GetStarWarsPeople).replace().to(mockEndPointFor_direct_GetStarWarsPeople);

		});

		
		mockEndPointFor_direct_GetStarWarsPeople.expectedMessageCount(1);

		// Start Camel context so that StarWarsOperationsBatch.ROUTE_ID will be reloaded with the Advices.
		camelContext.start();

		// call Adviced end point.
		producerTemplate.sendBody("direct:mockEndPointFor_StarWarsOperationsBatch_route_timerSWBatch", null);

		// Assert results.
		mockEndPointFor_direct_GetStarWarsPeople.assertIsSatisfied();
		
	}
	
}
