package com.horasan.routes.test;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.horasan.batch.StarWarsOperationsBatch;
import com.horasan.model.StarWarsPerson;
import com.horasan.routes.SaveStarWarsPersonRoute;
import com.horasan.services.StarWarsPeopleService;

@CamelSpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@UseAdviceWith
@MockEndpoints
public class SaveStarWarsPersonRouteTest {
	
	// route_timerSWBatch from timer (in StarWarsOperationsBatch.java) is not started.
	@MockBean
	private StarWarsOperationsBatch starWarsOperationsBatch;
	
	@Autowired
	private FluentProducerTemplate fluentProducerTemplate;

	@MockBean
	private StarWarsPeopleService mockPeopleService;
	
	@Captor
	ArgumentCaptor<StarWarsPerson> starWarsPeopleCaptor;
	
	@Autowired
	private CamelContext camelContext;
	
	@EndpointInject("mock:mockEndPointFor_save_processor")
	protected MockEndpoint mockEndPointFor_save_processor;
	
	/*
	Camel related items:
		FluentProducerTemplate
	Description:
		Route "direct:SaveStarWarsPerson" is called directly.
	 */
	@Test
	public void test_10_with_fluentProducerTemplate() {
		
		StarWarsPerson dummyStarWarsPeople = new StarWarsPerson();
		dummyStarWarsPeople.setName("dummyStarWarsPeople_name");
		
		Mockito.when(mockPeopleService.saveStarWarsPeople(ArgumentMatchers.any(StarWarsPerson.class))).thenReturn(dummyStarWarsPeople);
		
		camelContext.start();
		
		Exchange responseExchange = fluentProducerTemplate.to("direct:SaveStarWarsPerson")
				.withBody(dummyStarWarsPeople)
				.request(Exchange.class);
	}
	
	/*
	Camel related items:
		FluentProducerTemplate
	Description:
		Route "direct:SaveStarWarsPerson" is called directly.
		StarWarsPeopleService.saveStarWarsPeople method call is verified.
	 */
	@Test
	public void test_11_with_mockito_verify() {
		
		StarWarsPerson dummyStarWarsPeople = new StarWarsPerson();
		dummyStarWarsPeople.setName("dummyStarWarsPeople_name");
		
		Mockito.when(mockPeopleService.saveStarWarsPeople(ArgumentMatchers.any(StarWarsPerson.class))).thenReturn(dummyStarWarsPeople);
		
		camelContext.start();
		
		Exchange responseExchange = fluentProducerTemplate.to("direct:SaveStarWarsPerson")
				.withBody(dummyStarWarsPeople)
				.request(Exchange.class);
		
		Mockito.verify(mockPeopleService, Mockito.times(1)).saveStarWarsPeople(ArgumentMatchers.eq(dummyStarWarsPeople));
	}
	
	/*
	Camel related items:
		FluentProducerTemplate
	Description:
		Route "direct:SaveStarWarsPerson" is called directly.
		StarWarsPeopleService.saveStarWarsPeople method call is verified with ArgumentCaptor.
	 */
	@Test
	public void test_12_with_mockito_verify_argument_captors() {
		
		StarWarsPerson dummyStarWarsPeople = new StarWarsPerson();
		dummyStarWarsPeople.setName("dummyStarWarsPeople_name");
		
		Mockito.when(mockPeopleService.saveStarWarsPeople(ArgumentMatchers.any(StarWarsPerson.class))).thenReturn(dummyStarWarsPeople);
		
		camelContext.start();
		
		Exchange responseExchange = fluentProducerTemplate.to("direct:SaveStarWarsPerson")
				.withBody(dummyStarWarsPeople)
				.request(Exchange.class);
		
		Mockito.verify(mockPeopleService, Mockito.times(1)).saveStarWarsPeople(ArgumentMatchers.eq(dummyStarWarsPeople));
		
		Mockito.verify(mockPeopleService).saveStarWarsPeople(starWarsPeopleCaptor.capture());
		
		StarWarsPerson capturedStarWarsPeople = starWarsPeopleCaptor.getValue();
		
		Assertions.assertEquals(dummyStarWarsPeople.getName(), capturedStarWarsPeople.getName(), "received and processed StarWarsPeople are not the same.");

	}

	/*
	Camel related items:
		FluentProducerTemplate
		AdviceWithRouteBuilder.weaveById
	Description:
		1) Route "direct:SaveStarWarsPerson" is called directly.
		2) savePerson processor is replaced with mockEndPointFor_save_processor using AdviceWithRouteBuilder.weaveById.
		3) StarWarsPeopleService.saveStarWarsPeople method call is verified that it is NEVER called (check #2).
	 */
	@Test
	public void test_13_with_mockito_verify_argument_captors() throws Exception {
		
		StarWarsPerson dummyStarWarsPeople = new StarWarsPerson();
		dummyStarWarsPeople.setName("dummyStarWarsPeople_name");
		
		Mockito.when(mockPeopleService.saveStarWarsPeople(ArgumentMatchers.any(StarWarsPerson.class))).thenReturn(dummyStarWarsPeople);
		
		AdviceWith.adviceWith(camelContext, SaveStarWarsPersonRoute.ROUTE_ID, routeBuilder -> {
			
			routeBuilder.weaveById(SaveStarWarsPersonRoute.ID_processor_for_savePerson_operation)
			.replace()
			.to(mockEndPointFor_save_processor);
		});
		
		camelContext.start();
		
		Exchange responseExchange = fluentProducerTemplate.to("direct:SaveStarWarsPerson")
				.withBody(dummyStarWarsPeople)
				.request(Exchange.class);
		
		Mockito.verify(mockPeopleService, Mockito.never()).saveStarWarsPeople(ArgumentMatchers.eq(dummyStarWarsPeople));	

	}
	
}
